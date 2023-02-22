package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.repository.MemberRepository;

public class LoginService {
    private MemberRepository memberRepository;

    public Member login(String nickname, String password) {
        return memberRepository.findByNickname(nickname)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

}
