package LikeLion.TodaysLunch.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {

  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String getThumbnailPath(String imageName) {
    return amazonS3Client.getUrl(bucket, imageName).toString();
  }

  // 현재는 Prefix의 길이가 모든 S3 이미지 파일에서 똑같아서 sunstring 함수를 사용했으나 추후 문제가 발생할 가능성이 있어 보임
  public void delete(String imageName){
    try {
      amazonS3Client.deleteObject(this.bucket, imageName.substring(60));
    } catch (AmazonServiceException e) {
      System.err.println(e.getErrorMessage());
    }
  }


  // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
  public String upload(MultipartFile multipartFile, String dirName) throws IOException {
    File uploadFile = convert(multipartFile)
        .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
    return upload(uploadFile, dirName);
  }

  private String upload(File uploadFile, String dirName) {
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    String fileName = dirName + "/" + today + "/" + uuidFileName(uploadFile.getName());
    String uploadImageUrl = putS3(uploadFile, fileName);

    removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

    return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
  }

  private String uuidFileName(String originalFileName) {
    UUID uuid = UUID.randomUUID();
    return uuid.toString() + '_' + originalFileName;
  }

  private String putS3(File uploadFile, String fileName) {
    amazonS3Client.putObject(
        new PutObjectRequest(bucket, fileName, uploadFile)
            .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
    );
    return amazonS3Client.getUrl(bucket, fileName).toString();
  }

  private void removeNewFile(File targetFile) {
    if(targetFile.delete()) {
      log.info("파일이 삭제되었습니다.");
    }else {
      log.info("파일이 삭제되지 못했습니다.");
    }
  }

  private Optional<File> convert(MultipartFile file) throws  IOException {
    File convertFile = new File(file.getOriginalFilename());
    if(convertFile.createNewFile()) {
      try (FileOutputStream fos = new FileOutputStream(convertFile)) {
        fos.write(file.getBytes());
      }
      return Optional.of(convertFile);
    }
    return Optional.empty();
  }
}
