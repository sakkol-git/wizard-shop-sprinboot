package com.codewithsakkol.wizard.store.orders;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {
    @NotNull(message = "cart ID is required.")
    private UUID cartId;
}
