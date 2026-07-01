package com.codewithsakkol.wizard.store.orders;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        var orderDto = orderService.getOrders();
        return ResponseEntity.ok(orderDto);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getAllOrders(@PathVariable Long orderId){
        var orderDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }


}
