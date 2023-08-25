package LikeLion.TodaysLunch.image.repository;

import LikeLion.TodaysLunch.image.domain.ImageUrl;
import LikeLion.TodaysLunch.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUrlRepository extends JpaRepository<ImageUrl, Long> {
  Optional<ImageUrl> findByImageUrl(String imageUrl);
  List<ImageUrl> findAllByMember(Member member);
}
