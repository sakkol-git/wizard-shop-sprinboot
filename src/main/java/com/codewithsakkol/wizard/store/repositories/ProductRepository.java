package com.codewithsakkol.wizard.store.repositories;

import com.codewithsakkol.wizard.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(byte categoryId);
}