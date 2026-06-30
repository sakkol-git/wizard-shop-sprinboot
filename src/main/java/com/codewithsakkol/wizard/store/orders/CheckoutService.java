package com.codewithsakkol.wizard.store.orders;

import com.codewithsakkol.wizard.store.carts.CartService;
import com.codewithsakkol.wizard.store.auth.AuthService;


import com.codewithsakkol.wizard.store.orders.CheckoutRequest;
import com.codewithsakkol.wizard.store.orders.CheckoutRespond;
import com.codewithsakkol.wizard.store.payments.WebhookRequest;
import com.codewithsakkol.wizard.store.orders.Order;
import com.codewithsakkol.wizard.store.payments.PaymentStatus;
import com.codewithsakkol.wizard.store.common.PaymentException;
import com.codewithsakkol.wizard.store.common.ResourceNotFoundException;
import com.codewithsakkol.wizard.store.carts.CartRepository;
import com.codewithsakkol.wizard.store.orders.OrderRepository;
import com.codewithsakkol.wizard.store.payments.itf.PaymentGateway;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutRespond checkout(CheckoutRequest request) {
        var cart = cartRepository.findById(request.getCartId()).orElse(null);
        if(cart == null) {
            throw new ResourceNotFoundException("Cart", "ID", request.getCartId());
        }
        if (cart.getCartItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart Items", "Is Empty", cart.getCartItems());
        }
        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        try {
            // Create Checkout session
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());

            return new CheckoutRespond(order.getId(), session.getCheckoutUrl());
        }catch (PaymentException e) {
            orderRepository.delete(order);
            System.out.println(e.getMessage());
            throw e;
        }
    }


    public void handleWebhook(WebhookRequest request){
        paymentGateway.parseWebhookRequest(request)
                .ifPresent(
                        paymentResult -> {
                            var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                            order.setStatus(paymentResult.getPaymentStatus());
                            orderRepository.save(order);
                        }
                );
    }
}
