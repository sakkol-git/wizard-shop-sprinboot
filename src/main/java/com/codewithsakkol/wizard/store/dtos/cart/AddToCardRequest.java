package com.codewithsakkol.wizard.store.dtos.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCardRequest {
    @NotNull
    private Long productId;

}
