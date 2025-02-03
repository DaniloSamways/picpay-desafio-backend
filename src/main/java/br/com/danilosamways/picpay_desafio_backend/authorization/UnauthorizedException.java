package br.com.danilosamways.picpay_desafio_backend.authorization;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
