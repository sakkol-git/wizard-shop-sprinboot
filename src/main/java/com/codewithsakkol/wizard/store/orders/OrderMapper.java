package com.codewithsakkol.wizard.store.orders;

import com.codewithsakkol.wizard.store.orders.OrderDto;
import com.codewithsakkol.wizard.store.orders.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
