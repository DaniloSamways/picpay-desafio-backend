package br.com.danilosamways.picpay_desafio_backend.authorization;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.com.danilosamways.picpay_desafio_backend.transaction.Transaction;

@Service
public class AuthorizerService {
    private final RestClient restClient;

    public AuthorizerService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://util.devi.tools/api/v2/authorize")
                .build();
    }

    public void authorize(Transaction transaction) {
        ResponseEntity<Authorization> response = restClient.get()
                .retrieve()
                .toEntity(Authorization.class);

        if (response.getStatusCode().isError() || !response.getBody().isAuthorized()) {
            throw new UnauthorizedException("Unauthorized transaction");
        }
    }
}
