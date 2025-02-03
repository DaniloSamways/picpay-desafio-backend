package br.com.danilosamways.picpay_desafio_backend.authorization;

public record Authorization(
        String status,
        ResponseData data) {

    public record ResponseData(
            Boolean authorization) {
    }

    public boolean isAuthorized() {
        return data().authorization();
    }
}
