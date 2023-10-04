package kpfu.crm.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import kpfu.crm.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
@RestControllerAdvice
public class AppExceptionHandler {

    private final ObjectMapper jsonMapper;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handle(Exception e) {
        e.printStackTrace();
        return ResponseFactory.fail(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handle(MethodArgumentNotValidException e) {
        String message = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(joining("; "));

        e.printStackTrace();
        return ResponseFactory.fail(message);
    }

    @SneakyThrows
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Map<String, Object>> handle(TransactionSystemException e) {
        Throwable mostSpecificCause = e.getMostSpecificCause();


        if (mostSpecificCause instanceof ConstraintViolationException constraintException) {
            List<ConstraintValidationMessage> messages = constraintException.getConstraintViolations().stream()
                    .map(ConstraintValidationMessage::new)
                    .toList();

            return ResponseFactory.fail(jsonMapper.writeValueAsString(messages));
        }

        e.printStackTrace();
        return ResponseFactory.fail(e.getMessage());
    }

    private record ConstraintValidationMessage(String property, String message) {

        ConstraintValidationMessage(ConstraintViolation<?> violation) {
            this(violation.getPropertyPath().toString(), violation.getMessage());
        }
    }
}
