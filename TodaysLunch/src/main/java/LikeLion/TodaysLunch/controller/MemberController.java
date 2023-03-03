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
        try{
            memberService.join(memberDto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.ok("성공적으로 가입했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginDto memberDto) {
        TokenDto tokenDto;
        try{
            tokenDto = memberService.login(memberDto);
            return ResponseEntity.ok(tokenDto);
        } catch (IllegalArgumentException e){
            String errorMessage = e.getMessage();
            if(errorMessage.equals("존재하지 않는 회원입니다.")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
            if(errorMessage.equals("비밀번호가 일치하지 않습니다.")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
            }
        }
    }

    @PostMapping("/logout-member")
    public ResponseEntity<String> logout(@RequestHeader String Authorization) {
        try{
            memberService.logout(Authorization);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.ok("성공적으로 로그아웃했습니다.");
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> myPage(@AuthenticationPrincipal Member member){
        try {
            MemberDto memberDto = memberService.getAuthenticatedMember(member);
            return ResponseEntity.ok(memberDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
