package com.codewithsakkol.wizard.store.repositories;

import com.codewithsakkol.wizard.store.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}