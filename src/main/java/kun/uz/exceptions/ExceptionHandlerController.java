package kun.uz.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        // write logic there
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        List<String> errors = new LinkedList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }

        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);

    }

<<<<<<< HEAD
    @ExceptionHandler({IllegalArgumentException.class ,AppBadRequestException.class, ResourceNotFoundException.class})
=======
    @ExceptionHandler({IllegalArgumentException.class ,AppBadRequestException.class})
>>>>>>> ff86d3875ead696e854446a49c98943d12e9089b
    public ResponseEntity<String> illegalArgument(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
