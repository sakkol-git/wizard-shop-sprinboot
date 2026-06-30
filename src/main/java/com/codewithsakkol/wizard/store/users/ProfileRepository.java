package com.codewithsakkol.wizard.store.users;

import com.codewithsakkol.wizard.store.users.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}