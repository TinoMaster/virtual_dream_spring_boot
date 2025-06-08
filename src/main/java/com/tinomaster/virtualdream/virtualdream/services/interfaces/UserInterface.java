package com.tinomaster.virtualdream.virtualdream.services.interfaces;

import com.tinomaster.virtualdream.virtualdream.dtos.UserDto;
import com.tinomaster.virtualdream.virtualdream.entities.User;

import java.util.List;

public interface UserInterface {

    public User findOrThrow(final long id);

    public boolean existAnyUser();

    public User saveUser(UserDto user);

    public User saveUser(User user);

    public List<User> getAllUsers();

    public List<User> getUnauthorizedUsers();

    public User getUserById(Long id);

    public User getUserByEmail(String email);

    public void activeUser(Long id);

    public void denyUser(Long id);

    public void removeUser(Long id);

    public void updateUser(Long id, User user);

    public String getLastLoginByEmail(String email);
}
