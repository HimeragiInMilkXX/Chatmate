package com.easyprofile.authlib.exception;

import cn.dev33.satoken.exception.SaTokenException;
import com.easyprofile.authlib.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthLibException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleAuthLibException(AuthLibException ex) {
        return ResponseEntity.status(ex.getHttpStatus())
            .body(ApiResponse.<Map<String, Object>>fail(ex.getMessage(), null));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleValidationException(Exception ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            methodArgumentNotValidException.getBindingResult().getFieldErrors()
                .forEach(fieldError -> details.put(fieldError.getField(), fieldError.getDefaultMessage()));
        } else if (ex instanceof BindException bindException) {
            bindException.getBindingResult().getFieldErrors()
                .forEach(fieldError -> details.put(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.fail("Validation failed", details));
    }

    @ExceptionHandler(SaTokenException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleSaTokenException(SaTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.<Map<String, Object>>fail("Authentication required", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleUnknownException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.<Map<String, Object>>fail("Internal server error", null));
    }
}
