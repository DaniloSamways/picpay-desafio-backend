package br.com.danilosamways.picpay_desafio_backend.notification;

import org.springframework.data.relational.core.mapping.Table;

@Table("NOTIFICATIONS")
public record Notification(
        String status,
        String message) {

}
