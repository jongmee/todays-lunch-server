package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Member;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

public class HomeController {
    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = "loginMember", required = false) Member loginMember, Model model) {

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
