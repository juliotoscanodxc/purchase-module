package org.example.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.example.dto.PaymentReceipt;
import org.example.dto.PurchaseOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PurchaseListenerConfig {

    @Value(value = "${spring.kafka.bootstrapAddress}")
    private String bootstrapServersConfig;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    @Bean(name = "consumerPurchaseFactory")
    public ConsumerFactory<Integer, PurchaseOrder> consumerPurchaseFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new IntegerDeserializer(),
                new JsonDeserializer<>(PurchaseOrder.class));
    }

    @Bean(name = "consumerPaymentFactory")
    public ConsumerFactory<Integer, PaymentReceipt> consumerPaymentFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new IntegerDeserializer(),
                new JsonDeserializer<>(PaymentReceipt.class));
    }

    @Bean(name = "purchaseListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Integer, PurchaseOrder> purchaseListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, PurchaseOrder> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerPurchaseFactory());
        return factory;
    }

    @Bean(name = "paymentListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Integer, PaymentReceipt> paymentListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, PaymentReceipt> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerPaymentFactory());
        return factory;
    }
}
