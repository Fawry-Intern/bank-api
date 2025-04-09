package com.fawry.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {


    @Bean
    public NewTopic resetTopic() {
        return TopicBuilder
                .name("reset-password-events")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic cancelPaymentTopic() {
        return TopicBuilder
                .name("payment-canceled-events")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic createdPaymentTopic() {
        return TopicBuilder
                .name("payment-created-events")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
