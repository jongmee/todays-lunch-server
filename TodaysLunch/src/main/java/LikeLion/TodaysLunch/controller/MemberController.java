package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.dto.MemberDto;
import LikeLion.TodaysLunch.dto.TokenDto;
import LikeLion.TodaysLunch.service.login.MemberService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public Long join(@Valid @RequestBody MemberDto memberDto) {
        return memberService.join(memberDto);
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody MemberDto memberDto) {
        return memberService.login(memberDto);
    }

    @PutMapping("/logout-member")
    public ResponseEntity<String> logout(@RequestHeader("token") TokenDto tokenDto) {
        try{
            memberService.logout(tokenDto);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.ok("성공적으로 로그아웃했습니다.");
    }

}
