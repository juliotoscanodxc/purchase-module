package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enuns.PurchaseStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PurchaseReceipt {
    private String username;

    private double debitAmount;

    private PurchaseStatus status;
}
