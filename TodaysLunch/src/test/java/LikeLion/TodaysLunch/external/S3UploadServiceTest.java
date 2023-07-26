package LikeLion.TodaysLunch.external;

import LikeLion.TodaysLunch.external.S3UploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class S3UploadServiceTest {
  @Autowired
  private S3UploadService s3UploadService;

  @Test
  public void findImg() {
    String imgPath = s3UploadService.getThumbnailPath("IMG_3293.jpg");
    System.out.println(imgPath);
  }

}
