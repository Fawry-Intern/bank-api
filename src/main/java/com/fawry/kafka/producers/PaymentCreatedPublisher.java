package com.fawry.kafka.producers;

import com.fawry.kafka.events.PaymentCreatedEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentCreatedPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentUpdatedEvent(PaymentCreatedEventDTO paymentCreatedEventDTO) {
        log.info("Publish order event created to store to reserve the orderItems {}: ", paymentCreatedEventDTO);
        Message<PaymentCreatedEventDTO> message =
                MessageBuilder
                        .withPayload(paymentCreatedEventDTO)
                        .setHeader(TOPIC, "payment-created-events")
                        .build();
        kafkaTemplate.send(message);
    }
}
