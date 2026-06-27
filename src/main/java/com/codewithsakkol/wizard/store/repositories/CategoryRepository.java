package com.codewithsakkol.wizard.store.repositories;

import com.codewithsakkol.wizard.store.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}