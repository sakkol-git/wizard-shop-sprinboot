package com.codewithsakkol.wizard.store.payments;

import com.codewithsakkol.wizard.store.payments.WebhookRequest;
import com.codewithsakkol.wizard.store.orders.Order;
import com.codewithsakkol.wizard.store.orders.OrderItem;
import com.codewithsakkol.wizard.store.payments.PaymentStatus;
import com.codewithsakkol.wizard.store.common.PaymentException;
import com.codewithsakkol.wizard.store.orders.OrderRepository;
import com.codewithsakkol.wizard.store.payments.itf.PaymentGateway;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StripePaymentGateway implements PaymentGateway {

    private final OrderRepository orderRepository;
    @Value("${client.url}")
    private String webUrl;
    @Value("${stripe.webhookKey}")
    private String webhookKey;
    
    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(webUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(webUrl + "/checkout-cancel")
                    .putMetadata("order_id", order.getId().toString());
            order.getItems().forEach(item -> {
                SessionCreateParams.LineItem lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        } catch (StripeException ex) {
            System.out.println(ex.getMessage());
            throw new PaymentException("Failed to process stripe payment");
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            var event =  Webhook.constructEvent(request.getPayload(), request.getHeader().get("stripe-signature"), webhookKey);
            return switch (event.getType()) {
                case "payment_intent.succeeded" ->
                    //Update order status (Paid)
                    Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));
                case  "payment_intent.failed" ->
                    //Update order status (Failed)
                   Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));
                default ->
                    Optional.empty();
            };
        } catch (SignatureVerificationException e) {
            throw new PaymentException("Could not verify Signature");
        }
    }

    private Long extractOrderId(Event event) {
        var stripeData = event.getDataObjectDeserializer().getObject().orElseThrow(
                ()->new PaymentException("Could not deserialize Stripe event")
        );
        var paymentIntent = (PaymentIntent) stripeData;
            var order_id = paymentIntent.getMetadata().get("order_id");
            return Long.valueOf(order_id);
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity((long) item.getQuantity())
                .setPriceData(createPricedata(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPricedata(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount(item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(100))
                        .longValue())
                .setProductData(createProductData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .build();
    }
}
