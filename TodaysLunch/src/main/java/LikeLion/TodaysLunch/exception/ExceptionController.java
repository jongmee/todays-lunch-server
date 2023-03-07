package LikeLion.TodaysLunch.exception;

import java.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ExceptionController {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Object processValidationException(MethodArgumentNotValidException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),HttpStatus.BAD_REQUEST.value()));
  }
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> objectNotFoundException(RuntimeException ex){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
  }
  @ExceptionHandler(DuplicationException.class)
  public ResponseEntity<ErrorResponse> duplicationException(RuntimeException ex){
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value()));
  }
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> unauthorizedException(RuntimeException ex){
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value()));
  }
}
