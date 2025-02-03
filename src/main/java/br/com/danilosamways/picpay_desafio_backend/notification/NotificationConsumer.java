package br.com.danilosamways.picpay_desafio_backend.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.com.danilosamways.picpay_desafio_backend.authorization.AuthorizerService;
import br.com.danilosamways.picpay_desafio_backend.transaction.Transaction;

@Service
public class NotificationConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizerService.class);
    private RestClient restClient;

    public NotificationConsumer(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://util.devi.tools/api/v1/notify")
                .build();
    }

    @KafkaListener(topics = "transaction-notification", groupId = "picpay-desafio-backend")
    public void receiveNotification(Transaction transaction) {
        try {
            LOGGER.info("Notifying transaction: {}", transaction);

            ResponseEntity<Notification> response = restClient.post()
                    .retrieve()
                    .toEntity(Notification.class);

            Notification notification = response.getBody();

            if (response.getStatusCode().isError() || (notification != null && notification.status().equals("error")))
                throw new NotificationException("Error sending notification");

            LOGGER.info("Notification sent successfully: {}", transaction);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NotificationException("Error sending notification");
        }
    }
}
