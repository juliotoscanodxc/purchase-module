package org.example.service;

import org.example.dto.PaymentReceipt;
import org.example.dto.PurchaseOrder;

public interface PurchaseService {

    void requestPayment(PurchaseOrder purchaseOrder) throws InterruptedException;

    void approvePurchase(PaymentReceipt paymentReceipt);
}
