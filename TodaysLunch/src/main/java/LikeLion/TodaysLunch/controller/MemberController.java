package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.dto.MemberDto;
import LikeLion.TodaysLunch.service.login.MemberService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String login(@RequestBody MemberDto memberDto) {
        return memberService.login(memberDto);
    }

   /* @PutMapping("/user")
    public void updateMemberCategories
            (@RequestParam Member member, @RequestParam String locationCategory, @RequestParam String foodCategory){
        memberService.setLocationCategory(member,locationCategory);
        memberService.setFoodCategory(member,foodCategory);
    }

    @GetMapping("/user")
    public String createForm() {
        // 회원가입 html로 연결: method= post / nickname & password
        return null;
    }

    @PostMapping("/user")
    public ResponseEntity<String> createMember(@RequestParam String nickname, @RequestParam String password) {
        try{
            memberService.signUp(nickname, password);
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Success");
    }*/

}
