package LikeLion.TodaysLunch.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
  private String message;
  private Integer code;
  public ErrorResponse(String message, Integer code) {
    this.message = message;
    this.code = code;
  }
}
