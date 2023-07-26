package LikeLion.TodaysLunch.member.dto;

import LikeLion.TodaysLunch.member.domain.Member;
import LikeLion.TodaysLunch.member.dto.MemberDto;
import org.springframework.stereotype.Component;

@Component
public class MemberDtoMapper {

    public static MemberDto toDto(Member member) {
        MemberDto dto = new MemberDto();
        dto.setEmail(member.getEmail());
        dto.setNickname(member.getNickname());
        dto.setPassword(member.getPassword());
        dto.setImageUrl(member.getIcon());
        return dto;
    }
}
