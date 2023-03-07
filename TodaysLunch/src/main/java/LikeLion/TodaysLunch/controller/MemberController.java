package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.dto.MemberDto;
import LikeLion.TodaysLunch.dto.MemberJoinDto;
import LikeLion.TodaysLunch.dto.MemberLoginDto;
import LikeLion.TodaysLunch.dto.TokenDto;
import LikeLion.TodaysLunch.service.login.MemberService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> myPage(@AuthenticationPrincipal Member member){
        MemberDto memberDto = memberService.getAuthenticatedMember(member);
        return ResponseEntity.ok(memberDto);
    }
}
