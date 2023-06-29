package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.dto.FoodCategoryDto;
import LikeLion.TodaysLunch.dto.MemberDto;
import LikeLion.TodaysLunch.dto.MemberJoinDto;
import LikeLion.TodaysLunch.dto.MemberLoginDto;
import LikeLion.TodaysLunch.dto.MyFoodCategoryEditDto;
import LikeLion.TodaysLunch.dto.MyLocationCategoryEditDto;
import LikeLion.TodaysLunch.dto.MyPageDto;
import LikeLion.TodaysLunch.dto.TokenDto;
import LikeLion.TodaysLunch.service.login.MemberService;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody MemberJoinDto memberDto) {
        memberService.join(memberDto);
        return ResponseEntity.ok("성공적으로 가입했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginDto memberDto) {
        TokenDto tokenDto = memberService.login(memberDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/logout-member")
    public ResponseEntity<String> logout(@RequestHeader String Authorization) {
        memberService.logout(Authorization);
        return ResponseEntity.ok("성공적으로 로그아웃했습니다.");
    }

    @GetMapping("/mypage")
    public ResponseEntity<MyPageDto> myPage(@AuthenticationPrincipal Member member){
        MemberDto memberDto = memberService.getAuthenticatedMember(member);
        return ResponseEntity.ok(memberService.myPage(member));
    }

    @PatchMapping("/mypage/food-category")
    public ResponseEntity<Void> myFoodCategoryEdit(@AuthenticationPrincipal Member member,
        @RequestBody MyFoodCategoryEditDto categoryList){
        memberService.myFoodCategoryEdit(member, categoryList.getCategoryList());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/mypage/location-category")
    public ResponseEntity<Void> myLocationCategoryEdit(@AuthenticationPrincipal Member member,
        @RequestBody MyLocationCategoryEditDto categoryList){
        memberService.myLocationCategoryEdit(member, categoryList.getCategoryList());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/mypage/nickname")
    public ResponseEntity<Void> nicknameEdit(@AuthenticationPrincipal Member member,
        @RequestParam String nickname){
        memberService.nicknameEdit(member, nickname);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/mypage/icon")
    public ResponseEntity<Void> iconEdit(@AuthenticationPrincipal Member member,
        @RequestParam MultipartFile icon) throws IOException {
        memberService.iconEdit(member, icon);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
