package LikeLion.TodaysLunch.member.service;

import LikeLion.TodaysLunch.category.domain.FoodCategory;
import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.category.domain.LocationCategory;
import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.customized.domain.MemberFoodCategory;
import LikeLion.TodaysLunch.customized.domain.MemberLocationCategory;
import LikeLion.TodaysLunch.category.dto.FoodCategoryDto;
import LikeLion.TodaysLunch.category.dto.LocationCategoryDto;
import LikeLion.TodaysLunch.member.dto.AdminJoinDto;
import LikeLion.TodaysLunch.member.dto.JoinDto;
import LikeLion.TodaysLunch.member.dto.MemberJoinDto;
import LikeLion.TodaysLunch.member.dto.MemberLoginDto;
import LikeLion.TodaysLunch.member.dto.MyPageDto;
import LikeLion.TodaysLunch.member.dto.TokenDto;
import LikeLion.TodaysLunch.exception.DuplicationException;
import LikeLion.TodaysLunch.exception.NotFoundException;
import LikeLion.TodaysLunch.exception.UnauthorizedException;
import LikeLion.TodaysLunch.restaurant.domain.Restaurant;
import LikeLion.TodaysLunch.restaurant.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.category.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.image.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.category.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.customized.repository.MemberFoodCategoryRepository;
import LikeLion.TodaysLunch.customized.repository.MemberLocationCategoryRepository;
import LikeLion.TodaysLunch.member.repository.MemberRepository;
import LikeLion.TodaysLunch.customized.repository.MyStoreRepository;
import LikeLion.TodaysLunch.restaurant.repository.RestaurantContributorRepository;
import LikeLion.TodaysLunch.review.domain.Review;
import LikeLion.TodaysLunch.review.repository.ReviewRepository;
import LikeLion.TodaysLunch.external.S3UploadService;
import LikeLion.TodaysLunch.external.JwtTokenProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private S3UploadService s3UploadService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final LocationCategoryRepository locationCategoryRepository;
    private final MemberFoodCategoryRepository memberFoodCategoryRepository;
    private final MemberLocationCategoryRepository memberLocationCategoryRepository;
    private final DataJpaRestaurantRepository restaurantRepository;
    private final RestaurantContributorRepository restaurantContributorRepository;
    private final MyStoreRepository myStoreRepository;
    private final ReviewRepository reviewRepository;
    private final ImageUrlRepository imageUrlRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public void join(MemberJoinDto memberDto) {
        validateDuplication(memberDto);

        List<FoodCategory> foodCategoryList = memberDto.getFoodCategoryList()
            .stream()
            .map(f->foodCategoryRepository.findByName(f)
                .orElseThrow(() -> new NotFoundException("음식 카테고리")))
            .collect(Collectors.toList());

        List<LocationCategory> locationCategoryList = memberDto.getLocationCategoryList()
            .stream()
            .map(l->locationCategoryRepository.findByName(l)
                .orElseThrow(() -> new NotFoundException("위치 카테고리")))
            .collect(Collectors.toList());

        Member member = memberDto.toEntity(passwordEncoder.encode(memberDto.getPassword()));

        memberRepository.save(member);

        for(FoodCategory foodCategory: foodCategoryList){
            MemberFoodCategory memberFoodCategory = MemberFoodCategory.builder()
                .foodCategory(foodCategory)
                .member(member)
                .build();
            memberFoodCategoryRepository.save(memberFoodCategory);
        }

        for(LocationCategory locationCategory: locationCategoryList){
            MemberLocationCategory memberLocationCategory = MemberLocationCategory.builder()
                .locationCategory(locationCategory)
                .member(member)
                .build();
            memberLocationCategoryRepository.save(memberLocationCategory);
        }

    }

    public TokenDto.LoginToken login(MemberLoginDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.getEmail())
            .orElseThrow(() -> new UnauthorizedException("이메일을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new UnauthorizedException("회원정보의 비밀번호와 일치하지 않습니다.");
        }

        TokenDto.LoginToken tokenDto = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        redisTemplate.opsForValue()
            .set(member.getEmail(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresTime(), TimeUnit.MILLISECONDS);
        tokenDto.setId(member.getId());
        tokenDto.setTemporaryPw(member.getTemporaryPw());

        return tokenDto;
    }

    public TokenDto.LoginToken refresh(TokenDto.Refresh refreshDto) {
        jwtTokenProvider.validateToken(refreshDto.getRefreshToken());

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshDto.getAccessToken());

        String refreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if(!refreshToken.equals(refreshDto.getRefreshToken())){
            throw new UnauthorizedException("Refresh Token 정보가 일치하지 않습니다.");
        }

        Member member = memberRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new NotFoundException("유저"));

        TokenDto.LoginToken tokenDto = jwtTokenProvider.createToken(authentication.getName(), member.getRoles());
        redisTemplate.opsForValue()
            .set(member.getEmail(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresTime(), TimeUnit.MILLISECONDS);
        tokenDto.setId(member.getId());
        tokenDto.setTemporaryPw(member.getTemporaryPw());

        return tokenDto;
    }

    public void logout(String token) {
        jwtTokenProvider.validateToken(token);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            redisTemplate.delete(authentication.getName());
        }
    }

    public MyPageDto  myPage(Member member) {
        List<LocationCategoryDto> locationCategoryList = memberLocationCategoryRepository.findAllByMember(member)
            .stream()
            .map(MemberLocationCategory::getLocationCategory)
            .map(LocationCategoryDto::fromEntity)
            .collect(Collectors.toList());

        List<FoodCategoryDto> foodCategoryList = memberFoodCategoryRepository.findAllByMember(member)
            .stream()
            .map(MemberFoodCategory::getFoodCategory)
            .map(FoodCategoryDto::fromEntity)
            .collect(Collectors.toList());

        Integer myJudgeCount = restaurantRepository.findAllByRegistrantAndJudgement(member, true).size();
        Integer participationCount = restaurantRepository.findAllByRegistrantAndJudgement(member, false).size();
        Integer contributionCount = restaurantContributorRepository.findAllByMember(member).size();
        Integer myStoreCount = myStoreRepository.findAllByMember(member).size();
        Integer reviewCount = reviewRepository.findAllByMember(member).size();

        return MyPageDto.fromEntity(member, foodCategoryList, locationCategoryList, myJudgeCount, participationCount, myStoreCount, reviewCount, contributionCount);
    }

    public void myFoodCategoryEdit(Member member, List<String> categoryList) {;
        List<FoodCategory> newCategoryList = categoryList.stream()
            .map(f->foodCategoryRepository.findByName(f)
                .orElseThrow(() -> new NotFoundException("음식 카테고리")))
            .collect(Collectors.toList());

        List<FoodCategory> existingCategoryList = memberFoodCategoryRepository.findAllByMember(member)
            .stream()
            .map(MemberFoodCategory::getFoodCategory)
            .collect(Collectors.toList());

        List<FoodCategory> temporaryList = new ArrayList<>(existingCategoryList);
        existingCategoryList.removeAll(newCategoryList);
        newCategoryList.removeAll(temporaryList);

        for(FoodCategory obj: existingCategoryList){
            MemberFoodCategory memberFoodCategory = memberFoodCategoryRepository.findByFoodCategoryAndMember(obj, member).get();
            memberFoodCategoryRepository.delete(memberFoodCategory);
        }

        for(FoodCategory obj: newCategoryList){
            MemberFoodCategory memberFoodCategory = MemberFoodCategory.builder()
                .foodCategory(obj)
                .member(member)
                .build();
            memberFoodCategoryRepository.save(memberFoodCategory);
        }
    }

    public void myLocationCategoryEdit(Member member, List<String> categoryList) {
        List<LocationCategory> newCategoryList = categoryList.stream()
            .map(f->locationCategoryRepository.findByName(f)
                .orElseThrow(() -> new NotFoundException("위치 카테고리")))
            .collect(Collectors.toList());

        List<LocationCategory> existingCategoryList = memberLocationCategoryRepository.findAllByMember(member)
            .stream()
            .map(MemberLocationCategory::getLocationCategory)
            .collect(Collectors.toList());

        List<LocationCategory> temporaryList = new ArrayList<>(existingCategoryList);
        existingCategoryList.removeAll(newCategoryList);
        newCategoryList.removeAll(temporaryList);

        for(LocationCategory obj: existingCategoryList){
            MemberLocationCategory memberLocationCategory = memberLocationCategoryRepository.findByLocationCategoryAndMember(obj, member).get();
            memberLocationCategoryRepository.delete(memberLocationCategory);
        }

        for(LocationCategory obj: newCategoryList){
            MemberLocationCategory memberLocationCategory = MemberLocationCategory.builder()
                .locationCategory(obj)
                .member(member)
                .build();
            memberLocationCategoryRepository.save(memberLocationCategory);
        }
    }

    public void nicknameEdit(Member member, String nickname) {
        member.updateNickname(nickname);
        memberRepository.save(member);
    }

    public void iconEdit(Member member, MultipartFile icon) throws IOException {
        // 기존 아이콘 삭제
        ImageUrl imageUrl = member.getIcon();
        if(imageUrl != null){
            s3UploadService.delete(imageUrl.getImageUrl());
            Long id = imageUrl.getId();
            ImageUrl del = imageUrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("유저의 아이콘"));
            member.updateIcon(null);
            imageUrlRepository.delete(del);
        }

        // 새 아이콘 등록
        if(icon != null && !icon.isEmpty()){
            // s3에 이미지 저장
            String savedUrl = s3UploadService.upload(icon, "user");
            // image url을 db에 저장
            String originalName = icon.getOriginalFilename();
            ImageUrl userImage = ImageUrl.builder()
                .originalName(originalName)
                .imageUrl(savedUrl)
                .member(member)
                .build();

            imageUrlRepository.save(userImage);
            // 멤버에 저장
            member.updateIcon(userImage);
        }
        memberRepository.save(member);
    }

    public Member checkExistingEmail(String email){
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("해당 이메일로 가입된 유저"));
    }

    public void changePassword(Member member, String password, Boolean isTemporary){
        if(isTemporary){
            member.setTemporaryPw(true);
        }
        else if(member.getTemporaryPw()){
            member.setTemporaryPw(false);
        }
        member.updatePassword(passwordEncoder.encode(password));
        memberRepository.save(member);
    }

    public Boolean checkPassword(Member member, String password){
        if(passwordEncoder.matches(password, member.getPassword())){
            return true;
        }
        return false;
    }

    public void adminJoin(AdminJoinDto joinDto){
        validateDuplication(joinDto);

        Member member = joinDto.toEntity(passwordEncoder.encode(joinDto.getPassword()));

        memberRepository.save(member);
    }

    public void deleteAccount(Member member){
        List<Restaurant> restaurants = restaurantRepository.findAllByRegistrant(member);
        for(Restaurant restaurant: restaurants)
            restaurant.setNullRegistrant();

        List<ImageUrl> images = imageUrlRepository.findAllByMember(member);
        for(ImageUrl image: images)
            image.setMember(null);

        if(member.getIcon() != null)
            imageUrlRepository.delete(member.getIcon());

        memberRepository.delete(member);
    }

    private void validateDuplication(JoinDto memberDto) {
        if (memberRepository.findByEmail(memberDto.getEmail()).isPresent()) {
            throw new DuplicationException("이메일");
        }
    }

}