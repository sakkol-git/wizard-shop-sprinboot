package com.codewithsakkol.wizard.store.carts;

import com.codewithsakkol.wizard.store.carts.CartItemDto;
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
