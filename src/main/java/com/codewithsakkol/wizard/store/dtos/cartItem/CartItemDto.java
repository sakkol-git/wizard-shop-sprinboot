package com.codewithsakkol.wizard.store.dtos.cartItem;

import com.codewithsakkol.wizard.store.dtos.product.CartProductDto;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CartItemDto {
    private CartProductDto product;
    private int quantity;
    private BigDecimal totalPrice;

}
