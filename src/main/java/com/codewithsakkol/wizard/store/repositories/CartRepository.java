package com.codewithsakkol.wizard.store.repositories;

import com.codewithsakkol.wizard.store.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    @Query("select c from Cart c where c.id=?1")
    Optional<Cart> getCartWithItems( @Param("cartId") UUID cartID);
}