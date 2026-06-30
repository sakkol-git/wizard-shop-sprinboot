package com.codewithsakkol.wizard.store.orders;

import com.codewithsakkol.wizard.store.orders.Order;
import com.codewithsakkol.wizard.store.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findByCustomer(User customer);

}