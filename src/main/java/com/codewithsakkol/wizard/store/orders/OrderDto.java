package com.codewithsakkol.wizard.store.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class OrderDto {
    private long id;
    private String status;
    private LocalDateTime createAt;
    private List<OrderItemDto> items;


}
