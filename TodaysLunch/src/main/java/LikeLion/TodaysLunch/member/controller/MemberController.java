package LikeLion.TodaysLunch.member.controller;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.member.dto.MemberJoinDto;
import LikeLion.TodaysLunch.member.dto.MemberLoginDto;
import LikeLion.TodaysLunch.customized.dto.MyFoodCategoryEditDto;
import LikeLion.TodaysLunch.customized.dto.MyLocationCategoryEditDto;
import LikeLion.TodaysLunch.member.dto.MyPageDto;
import LikeLion.TodaysLunch.member.dto.TokenDto;
import LikeLion.TodaysLunch.member.service.EmailService;
import LikeLion.TodaysLunch.member.service.MemberService;
import java.io.IOException;
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
    private final EmailService emailService;

    @Autowired
    public MemberController(MemberService memberService, EmailService emailService) {
        this.memberService = memberService;
        this.emailService = emailService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody MemberJoinDto memberDto) {
        memberService.join(memberDto);
        return ResponseEntity.ok("성공적으로 가입했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginDto memberDto) {
        TokenDto.LoginToken tokenDto = memberService.login(memberDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/logout-member")
    public ResponseEntity<String> logout(@RequestHeader String Authorization) {
        memberService.logout(Authorization);
        return ResponseEntity.ok("성공적으로 로그아웃했습니다.");
    }

    @GetMapping("/mypage")
    public ResponseEntity<MyPageDto> myPage(@AuthenticationPrincipal Member member){
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
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenDto.Refresh memberDto) {
        TokenDto.LoginToken tokenDto = memberService.refresh(memberDto);
        return ResponseEntity.ok(tokenDto);
    }
    @PostMapping("/email-verification/send-code")
    public ResponseEntity<?> sendEmailCode(@RequestParam String email) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(emailService.sendEmailVerifyMessage(email));
    }

    @PostMapping("/find-pw")
    public ResponseEntity<Void> sendTemporaryPw(@RequestParam String email) throws Exception {
        Member member = memberService.checkExistingEmail(email);
        String password = emailService.sendTemporaryPassword(email);
        memberService.changePassword(member, password, true);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/change-pw")
    public ResponseEntity<Void> passwordEdit(@AuthenticationPrincipal Member member,
        @RequestParam String password){
        memberService.changePassword(member, password, false);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
