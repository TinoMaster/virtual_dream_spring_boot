package com.tinomaster.virtualdream.virtualDream.services;

import com.tinomaster.virtualdream.virtualDream.entities.User;
import com.tinomaster.virtualdream.virtualDream.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private User findOrThrow(final long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User by id " + id + " not found"));
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(long id) {
        return findOrThrow(id);
    }

    public void removeUser(long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void updateUser(long id, User user) {
        findOrThrow(id);
        userRepository.save(user);
    }
}
