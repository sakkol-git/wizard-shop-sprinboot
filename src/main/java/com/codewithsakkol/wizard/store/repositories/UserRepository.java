package com.codewithsakkol.wizard.store.repositories;

import com.codewithsakkol.wizard.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
