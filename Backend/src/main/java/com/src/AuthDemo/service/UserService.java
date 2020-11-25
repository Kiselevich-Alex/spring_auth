package com.src.AuthDemo.service;

import com.src.AuthDemo.model.User;
import com.src.AuthDemo.model.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    List<User> getUsersByFilter(String firstName, String lastName);
    User getUserById(Integer userId);
    User getUserByName(String userName);
    User createUser(UserDTO userDto);
    User updateUser(UserDTO userDto);
    boolean validateUsername(String username);
    void deleteUser(Integer userId);
}
