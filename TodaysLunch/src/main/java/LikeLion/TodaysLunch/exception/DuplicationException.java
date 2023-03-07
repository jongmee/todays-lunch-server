package LikeLion.TodaysLunch.exception;

public class DuplicationException extends RuntimeException{

  public DuplicationException(String object) {
    super("중복된 "+object+"입니다.");
  }
}
