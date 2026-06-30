package com.codewithsakkol.wizard.store.orders;

import com.codewithsakkol.wizard.store.orders.CheckoutRequest;
import com.codewithsakkol.wizard.store.payments.WebhookRequest;
import com.codewithsakkol.wizard.store.orders.OrderRepository;
import com.codewithsakkol.wizard.store.orders.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
class CheckoutController {

    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;


    @PostMapping
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequest request)
    {
            return ResponseEntity.ok( checkoutService.checkout(request));
    }
    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader Map<String, String> header,
            @RequestBody String payload
    ){
        checkoutService.handleWebhook(new WebhookRequest(header, payload));
        return ResponseEntity.ok().build();
    }

}
