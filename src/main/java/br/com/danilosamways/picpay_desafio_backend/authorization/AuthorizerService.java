package br.com.danilosamways.picpay_desafio_backend.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.com.danilosamways.picpay_desafio_backend.transaction.Transaction;

@Service
public class AuthorizerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizerService.class);
    private final RestClient restClient;

    public AuthorizerService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://util.devi.tools/api/v2/authorize")
                .build();
    }

    public void authorize(Transaction transaction) {
        LOGGER.info("Authorizing transaction: {}", transaction);
        try {
            ResponseEntity<Authorization> response = restClient
                    .get()
                    .retrieve()
                    .toEntity(Authorization.class);

            if (response.getStatusCode().isError() || !response.getBody().isAuthorized())
                throw new UnauthorizedException("Unauthorized transaction");

            LOGGER.info("Authorized transaction: {}", transaction);
        } catch (Exception e) {
            throw new UnauthorizedException("Unauthorized transaction");
        }
    }
}
