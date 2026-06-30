package com.codewithsakkol.wizard.store.orders;

import com.codewithsakkol.wizard.store.orders.ProductDto;


import com.codewithsakkol.wizard.store.orders.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link OrderItem}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto{
    private ProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}