package com.codewithsakkol.wizard.store.payments;

import com.codewithsakkol.wizard.store.payments.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Data
public class PaymentResult {
    private Long orderId;
    private PaymentStatus paymentStatus;

    public PaymentResult(Long orderId, PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
    }
}
