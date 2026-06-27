package com.codewithsakkol.wizard.store.controller;

import com.codewithsakkol.wizard.store.dtos.user.RegisterUserRequest;
import com.codewithsakkol.wizard.store.dtos.user.UpdateUserRequest;
import com.codewithsakkol.wizard.store.dtos.user.UserRespond;
import com.codewithsakkol.wizard.store.entities.User;
import com.codewithsakkol.wizard.store.mapper.UserMapper;
import com.codewithsakkol.wizard.store.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @GetMapping
    public List<UserRespond> getAllUser(@RequestParam(required = false, defaultValue = "", name = "sort") String sort){
        if (!Set.of("name", "email").contains(sort)) sort = "name";
        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::userToUserDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRespond> getUser(@PathVariable Long id){
        User user =  userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }

        return  ResponseEntity.ok(userMapper.userToUserDto(user));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody RegisterUserRequest requestUser){
        User user = userMapper.toEntity(requestUser);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.userToUserDto(user));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest requestUser){
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            return  ResponseEntity.notFound().build();
        }

        userMapper.update(requestUser, user);
        userRepository.save(user);
        return  ResponseEntity.ok(userMapper.userToUserDto(user));

    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteUser(@PathVariable Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            return  ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

}
