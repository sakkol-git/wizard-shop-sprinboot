package com.codewithsakkol.wizard.store.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URL;

@Data
@AllArgsConstructor
public class CheckoutRespond {
    private Long orderId;
    private String checkoutUrl;
    
}
