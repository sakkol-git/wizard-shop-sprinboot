package com.codewithsakkol.wizard.store.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.codewithsakkol.wizard.store.common.ResourceNotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserRespond> getAllUsers(String sort) {
        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::userToUserDto)
                .toList();
    }

    public UserRespond getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.userToUserDto(user);
    }

    public UserRespond createUser(RegisterUserRequest requestUser) {
        User user = userMapper.toEntity(requestUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user = userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    public UserRespond updateUser(Long id, UpdateUserRequest requestUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userMapper.update(requestUser, user);
        user = userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }
}
