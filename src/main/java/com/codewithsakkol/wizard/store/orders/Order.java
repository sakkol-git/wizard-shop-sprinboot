package com.codewithsakkol.wizard.store.orders;

import com.codewithsakkol.wizard.store.carts.Cart;
import com.codewithsakkol.wizard.store.users.User;


import com.codewithsakkol.wizard.store.payments.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order",cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderItem> items = new ArrayList<>();

    public static Order fromCart(Cart cart, User customer){
        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setCustomer(customer);
        order.setStatus(PaymentStatus.PENDING);
        order.setCreateAt(LocalDateTime.now());

        cart.getCartItems().forEach(item -> {
            var orderItem = new OrderItem(order, item.getProduct(), item.getQuantity());
            order.items.add(orderItem);
        });

        return order;
    }


}