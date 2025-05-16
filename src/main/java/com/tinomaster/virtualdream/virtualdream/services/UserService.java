package com.tinomaster.virtualdream.virtualdream.services;

import com.tinomaster.virtualdream.virtualdream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualdream.dtos.UserDto;
import com.tinomaster.virtualdream.virtualdream.entities.Business;
import com.tinomaster.virtualdream.virtualdream.entities.User;
import com.tinomaster.virtualdream.virtualdream.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    private final UserRepository userRepository;

    private final BusinessService businessService;


    private User findOrThrow(final long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User by id " + id + " not found"));
    }

    public boolean existAnyUser() {
        return !userRepository.findAll().isEmpty();
    }

    @Transactional
    public User saveUser(UserDto userDto) {
        User user = mapper.map(userDto, User.class);

        if (userDto.getId() == null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            List<Business> existingBusinesses = new ArrayList<>();

            if (userDto.getBusinesses() != null && !userDto.getBusinesses().isEmpty()) {
                for (BusinessDto businessDto : userDto.getBusinesses()) {
                    Business existingBusiness = businessService.getBusinessById(businessDto.getId());

                    existingBusiness.getUsers().add(user);
                    existingBusinesses.add(existingBusiness);
                }
            }

            user.setBusinesses(existingBusinesses);
        }

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUnauthorizedUsers() {
        return userRepository.findByActiveFalse();
    }

    public User getUserById(Long id) {
        return this.findOrThrow(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User by email " + email + " not found"));
    }

    public void activeUser(Long id) {
        userRepository.activeUser(id);
    }

    @Transactional
    public void denyUser(Long id) {
        businessService.deleteBusinessesByUserId(id);
        userRepository.deleteById(id);
    }

    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void updateUser(Long id, User user) {
        findOrThrow(id);
        userRepository.save(user);
    }
}
