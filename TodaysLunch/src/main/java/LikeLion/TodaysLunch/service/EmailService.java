package LikeLion.TodaysLunch.service;

import java.util.Random;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmailService {
  @Autowired
  JavaMailSender emailSender;
  @Value("${AdminMail.id}")
  private String senderEmail;
  public static final String code = createKey();

  private MimeMessage createEmailVerifyMessage(String to)throws Exception{
    MimeMessage  message = emailSender.createMimeMessage();

    message.addRecipients(RecipientType.TO, to);
    message.setSubject("점메추 이메일 인증");

    String msgg="";
    msgg+= "<div style='margin:100px;'>";
    msgg+= "<h1> 점메추 이메일 인증 메일입니다. </h1>";
    msgg+= "<br>";
    msgg+= "<p>아래 코드를 이메일 인증 창에 입력해주세요<p>";
    msgg+= "<br>";
    msgg+= "<p>감사합니다!<p>";
    msgg+= "<br>";
    msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
    msgg+= "<h3 style='color:blue;'>이메일 인증 코드입니다.</h3>";
    msgg+= "<div style='font-size:130%'>";
    msgg+= "CODE : <strong>";
    msgg+= code+"</strong><div><br/> ";
    msgg+= "</div>";
    message.setText(msgg, "utf-8", "html");
    message.setFrom(new InternetAddress(senderEmail,"점메추"));

    return message;
  }

  public static String createEmailKey() {
    StringBuffer key = new StringBuffer();
    Random rnd = new Random();

    for (int i = 0; i < 8; i++) {
      int index = rnd.nextInt(3);

      switch (index) {
        case 0:
          key.append((char) ((int) (rnd.nextInt(26)) + 97));
          break;
        case 1:
          key.append((char) ((int) (rnd.nextInt(26)) + 65));
          break;
        case 2:
          key.append((rnd.nextInt(10)));
          break;
      }
    }

    return key.toString();
  }

  public String sendEmailMessage(String to)throws Exception {
    MimeMessage message = createEmailVerifyMessage(to);
    try{
      emailSender.send(message);
    }catch(MailException es){
      es.printStackTrace();
      throw new IllegalArgumentException();
    }
    return code;
  }
}
