package com.dien.userservice.service;

import com.dien.userservice.dto.AuthResponse;
import com.dien.userservice.dto.LoginRequest;
import com.dien.userservice.dto.RegisterRequest;
import com.dien.userservice.entity.User;
import com.dien.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User user) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setUsername(user.getUsername());
        existing.setPassword(user.getPassword());
        existing.setRole(user.getRole());

        return userRepository.save(existing);
    }

    public User register(RegisterRequest request){

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(request.getPassword());

        user.setRole("USER");

        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Wrong password");
        }

        return new AuthResponse("Login success");
    }

}