package LikeLion.TodaysLunch.dto;

import LikeLion.TodaysLunch.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberDtoMapper {

    public static MemberDto toDto(Member member) {
        MemberDto dto = new MemberDto();
        dto.setNickname(member.getNickname());
        dto.setPassword(member.getPassword());
        dto.setLocationCategory(member.getLocationCategory());
        dto.setFoodCategory(member.getFoodCategory());
        dto.setImageUrl(member.getIcon());
        return dto;
    }
}
