package com.codewithsakkol.wizard.store.dtos.cart;

import com.codewithsakkol.wizard.store.dtos.cartItem.CartItemDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
    private UUID id;
    private List<CartItemDto> Items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;

}
