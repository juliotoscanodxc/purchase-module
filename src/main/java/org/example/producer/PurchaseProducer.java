package org.example.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PurchaseReceipt;
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
public class PurchaseProducer {

    @Value(value = "${spring.kafka.producer.purchase.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<Integer, PurchaseReceipt> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void send(PurchaseReceipt purchaseReceipt) {
        Integer key = new Random().nextInt(1000000);

        ListenableFuture<SendResult<Integer, PurchaseReceipt>> future = kafkaTemplate.send(topic, key, purchaseReceipt);

        future.addCallback(new ListenableFutureCallback<SendResult<Integer, PurchaseReceipt>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send =" + purchaseReceipt + " due to : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<Integer, PurchaseReceipt> result) {
                log.info("Sent message=" + purchaseReceipt + " with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            }
        });
    }
}
