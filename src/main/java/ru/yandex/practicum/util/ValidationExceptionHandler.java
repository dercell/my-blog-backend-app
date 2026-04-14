package ru.yandex.practicum.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.yandex.practicum.util.exceptions.DataOperationException;
import ru.yandex.practicum.util.exceptions.StorageException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataOperationException.class)
    public ResponseEntity<String> handleSqlException(DataOperationException doe) {
        return ResponseEntity.internalServerError().body(doe.getMessage());
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<String> storageException(StorageException se) {
        return ResponseEntity.internalServerError().body(se.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "postId должен быть числом, получено: " + ex.getValue()
        ));
    }

}
