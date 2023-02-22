package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.dto.MemberDto;
import LikeLion.TodaysLunch.service.login.MemberService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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


}
