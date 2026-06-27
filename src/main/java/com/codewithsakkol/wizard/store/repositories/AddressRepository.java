package com.codewithsakkol.wizard.store.repositories;

import com.codewithsakkol.wizard.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}