package com.codewithsakkol.wizard.store.users;

import com.codewithsakkol.wizard.store.users.RegisterUserRequest;
import com.codewithsakkol.wizard.store.users.UpdateUserRequest;
import com.codewithsakkol.wizard.store.users.UserRespond;
import com.codewithsakkol.wizard.store.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserRespond userToUserDto(User user);
    User toEntity(RegisterUserRequest requestUser);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
