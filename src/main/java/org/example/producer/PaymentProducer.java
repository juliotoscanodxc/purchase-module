package org.example.producer;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.PaymentOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Random;

@Configuration
@Slf4j
public class PaymentProducer {

    @Value(value = "${spring.kafka.producer.payment.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<Integer, PaymentOrder> kafkaTemplate;

    public void send(PaymentOrder paymentOrder) {
        Integer key = new Random().nextInt(1000000);

        ListenableFuture<SendResult<Integer, PaymentOrder>> future = kafkaTemplate.send(topic, key, paymentOrder);

        future.addCallback(new ListenableFutureCallback<SendResult<Integer, PaymentOrder>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send =" + paymentOrder + " due to : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<Integer, PaymentOrder> result) {
                log.info("Sent message=" + paymentOrder + " with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            }
        });
    }
}
