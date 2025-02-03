package br.com.danilosamways.picpay_desafio_backend.authorization;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UnauthorizedExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handle(UnauthorizedException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}
