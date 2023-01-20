package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.PaymentOrder;
import org.example.dto.PaymentReceipt;
import org.example.dto.PurchaseOrder;
import org.example.dto.PurchaseReceipt;
import org.example.enuns.PurchaseStatus;
import org.example.producer.PaymentProducer;
import org.example.producer.PurchaseProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseProducer purchaseProducer;

    @Autowired
    private PaymentProducer paymentProducer;

    @Override
    public void requestPayment(PurchaseOrder purchaseOrder) throws InterruptedException {
        paymentProducer.send(generatePaymentOrder(purchaseOrder));
    }

    private PaymentOrder generatePaymentOrder(PurchaseOrder purchaseOrder) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUsername(purchaseOrder.getUsername());
        paymentOrder.setDebitAmount(purchaseOrder.getDebitAmount());
        return paymentOrder;
    }

    @Override
    public void approvePurchase(PaymentReceipt paymentReceipt) {
        purchaseProducer.send(generateReceipt(paymentReceipt));
    }

    private PurchaseReceipt generateReceipt(PaymentReceipt paymentReceipt) {
        PurchaseReceipt receipt = new PurchaseReceipt();
        receipt.setUsername(paymentReceipt.getUsername());
        receipt.setDebitAmount(paymentReceipt.getDebitAmount());
        receipt.setStatus(PurchaseStatus.SUCCESS);
        return receipt;
    }
}
