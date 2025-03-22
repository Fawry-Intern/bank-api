package com.fawry.kafka.producers;

import com.fawry.kafka.events.ResetPasswordEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC_NAME = "reset-password-events";
    public void produceResetPasswordEvent(ResetPasswordEvent event) {
        Message<ResetPasswordEvent> message =
                MessageBuilder.withPayload(event)
                        .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                        .build();
        log.info("send event to topic {} to partition {}", event);
        kafkaTemplate.send(message);
    }
}

