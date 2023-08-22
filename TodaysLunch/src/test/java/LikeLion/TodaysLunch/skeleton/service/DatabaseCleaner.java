package LikeLion.TodaysLunch.skeleton.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner implements InitializingBean {

  @Value("${test-constraint}")
  private String constraint;
  private final List<String> tableNames = new ArrayList<>();

  @PersistenceContext
  private EntityManager entityManager;

  @PostConstruct
  public void findDatabaseTableNames() {
    List<Object[]> tableInfos = entityManager.createNativeQuery("SHOW TABLES").getResultList();
    for (Object[] tableInfo : tableInfos) {
      String tableName = (String) tableInfo[0];
      tableNames.add(tableName);
    }
  }

  private void truncate() {
    entityManager.createNativeQuery("SET " + constraint + " = 0").executeUpdate();
    for (String tableName : tableNames)
      entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
    entityManager.createNativeQuery("SET " + constraint + " = 1").executeUpdate();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    findDatabaseTableNames();
  }

  @Transactional
  public void clear() {
    entityManager.clear();
    truncate();
  }
}
