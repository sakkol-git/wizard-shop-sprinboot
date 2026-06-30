package com.codewithsakkol.wizard.store.products;

import com.codewithsakkol.wizard.store.products.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}