package com.codewithsakkol.wizard.store.orders;

import com.codewithsakkol.wizard.store.auth.AuthService;


import com.codewithsakkol.wizard.store.orders.OrderDto;
import com.codewithsakkol.wizard.store.common.ResourceNotFoundException;
import com.codewithsakkol.wizard.store.orders.OrderMapper;
import com.codewithsakkol.wizard.store.orders.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getOrders(){
        var customer = authService.getCurrentUser();
        var orders = orderRepository.findByCustomer(customer).orElse(null);
        if (orders == null) {
            throw new ResourceNotFoundException("Order", "Customer", customer);
        }

        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public OrderDto getOrder(Long orderId){
        var order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw  new ResourceNotFoundException("Order", "Order", orderId);
        }
        return orderMapper.toDto(order);
    }
}
