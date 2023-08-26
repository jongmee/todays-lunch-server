package LikeLion.TodaysLunch.skeleton.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.metamodel.Type;
import javax.transaction.Transactional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner implements InitializingBean {

  @Value("${test-constraint}")
  private String constraint;
  @Value("${test-db}")
  private String testDb;
  private final List<String> tableNames = new ArrayList<>();

  @PersistenceContext
  private EntityManager entityManager;

  @PostConstruct
  public void findDatabaseTableNames() {
    if(testDb.equals("mysql"))
      entityManager.createNativeQuery("SHOW TABLES").getResultList().stream().forEach(name->tableNames.add(String.valueOf(name)));
    else if(testDb.equals("h2")){
      List<Object[]> tableInfos = entityManager.createNativeQuery("SHOW TABLES").getResultList();
      for (Object[] tableInfo : tableInfos) {
        String tableName = String.valueOf(tableInfo[0]);
        tableNames.add(tableName);
      }
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
