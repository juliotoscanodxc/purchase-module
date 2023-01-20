package org.example.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.example.dto.PaymentOrder;
import org.example.dto.PurchaseReceipt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PurchaseSenderConfig {

    @Value(value = "${spring.kafka.bootstrapAddress}")
    private String bootstrapServersConfig;

    @Bean
    public KafkaTemplate<Integer, PurchaseReceipt> purchaseTemplate() {
        return new KafkaTemplate<>(producerPurchaseReceiptFactory());
    }

    @Bean
    public KafkaTemplate<Integer, PaymentOrder> paymentTemplate() {
        return new KafkaTemplate<>(producerPaymentOrderFactory());
    }

    @Bean
    public ProducerFactory<Integer, PurchaseReceipt> producerPurchaseReceiptFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ProducerFactory<Integer, PaymentOrder> producerPaymentOrderFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }
}
