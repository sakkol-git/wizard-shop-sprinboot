package com.codewithsakkol.wizard.store.payments.itf;

import com.codewithsakkol.wizard.store.payments.WebhookRequest;
import com.codewithsakkol.wizard.store.orders.Order;
import com.codewithsakkol.wizard.store.payments.CheckoutSession;
import com.codewithsakkol.wizard.store.payments.PaymentResult;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest webhookRequest);
}
