package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enuns.PaymentStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentReceipt {

    private String username;

    private double debitAmount;

    private PaymentStatus status;
}
