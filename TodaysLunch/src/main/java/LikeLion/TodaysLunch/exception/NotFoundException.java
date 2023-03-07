package LikeLion.TodaysLunch.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String object) {
    super(object+"를 찾을 수 없습니다.");
  }
}
