package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Sale;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
  List<Sale> findByEndDateBefore(LocalDate todayDate); // 세일 종료일이 오늘보다 이전이면 삭제해야 함
}
