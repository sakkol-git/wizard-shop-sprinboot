package com.codewithsakkol.wizard.store.orders;

import com.codewithsakkol.wizard.store.auth.jwt.AuthJwtService;


import com.codewithsakkol.wizard.store.common.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final AuthJwtService authJwtService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getOrders(){
        var customer = authJwtService.getCurrentUser();
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
