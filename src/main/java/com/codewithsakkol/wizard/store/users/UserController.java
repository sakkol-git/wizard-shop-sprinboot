package com.codewithsakkol.wizard.store.users;

import com.codewithsakkol.wizard.store.users.RegisterUserRequest;
import com.codewithsakkol.wizard.store.users.UpdateUserRequest;
import com.codewithsakkol.wizard.store.users.UserRespond;
import com.codewithsakkol.wizard.store.users.User;
import com.codewithsakkol.wizard.store.users.Role;
import com.codewithsakkol.wizard.store.users.UserMapper;
import com.codewithsakkol.wizard.store.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import jakarta.validation.Valid;
import com.codewithsakkol.wizard.store.common.ResourceNotFoundException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserRespond> getAllUser(@RequestParam(required = false, defaultValue = "", name = "sort") String sort){
        if (!Set.of("name", "email").contains(sort)) sort = "name";
        return userService.getAllUsers(sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRespond> getUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<UserRespond> createUser(@Valid @RequestBody RegisterUserRequest requestUser){
        return ResponseEntity.ok(userService.createUser(requestUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRespond> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest requestUser){
        return ResponseEntity.ok(userService.updateUser(id, requestUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
