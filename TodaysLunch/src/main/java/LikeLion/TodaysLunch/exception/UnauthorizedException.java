package LikeLion.TodaysLunch.exception;

public class PasswordMismatchException extends RuntimeException{
  public PasswordMismatchException() {
    super("회원정보의 비밀번호와 일치하지 않습니다.");
  }
}
