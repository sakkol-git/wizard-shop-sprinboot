package com.codewithsakkol.wizard.store.users;

import com.codewithsakkol.wizard.store.users.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}