package org.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.dto.PaymentReceipt;
import org.example.dto.PurchaseOrder;
import org.example.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PurchaseListener {

    @Autowired
    private PurchaseService purchaseService;

    @KafkaListener(topics = "purchase-order-topic", groupId = "purchase-listener-group", containerFactory = "purchaseListenerContainerFactory")
    public void onReceivePurchaseOrder(ConsumerRecord<Integer, PurchaseOrder> consumerRecord) {

        log.info("Received message: " + consumerRecord);

        try {
            purchaseService.requestPayment(consumerRecord.value());

        } catch (InterruptedException e) {

            log.error("Error on process message: " + consumerRecord.value());

            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "payment-receipt-topic", groupId = "payment-listener-group", containerFactory = "paymentListenerContainerFactory")
    public void onReceivePaymentReceipt(ConsumerRecord<Integer, PaymentReceipt> consumerRecord) {

        log.info("Received message: " + consumerRecord);

        purchaseService.approvePurchase(consumerRecord.value());
    }
}
