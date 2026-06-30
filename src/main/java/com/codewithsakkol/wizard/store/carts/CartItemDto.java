package com.codewithsakkol.wizard.store.carts;

import com.codewithsakkol.wizard.store.carts.ProductDto;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CartItemDto {
    private ProductDto product;
    private int quantity;
    private BigDecimal totalPrice;

}
