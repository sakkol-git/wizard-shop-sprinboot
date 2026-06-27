package com.codewithsakkol.wizard.store.mapper;

import com.codewithsakkol.wizard.store.dtos.user.RegisterUserRequest;
import com.codewithsakkol.wizard.store.dtos.user.UpdateUserRequest;
import com.codewithsakkol.wizard.store.dtos.user.UserRespond;
import com.codewithsakkol.wizard.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserRespond userToUserDto(User user);
    User toEntity(RegisterUserRequest requestUser);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
