package com.src.AuthDemo.controller;


import com.src.AuthDemo.model.User;
import com.src.AuthDemo.model.dto.UserDTO;
import com.src.AuthDemo.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/manager/users")
    public ResponseEntity<List<User>> getAllUsers() {
        LOGGER.info("Controller method called to view all list of Users");
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/manager/users/filter")
    public ResponseEntity<List<User>> getUsersByFilter(@RequestParam(value="firstName", required = false) String firstName,
                                                       @RequestParam(value="lastName", required = false) String lastName) {
        LOGGER.info("Controller method called to view all Users by Filter");
        return ResponseEntity.ok().body(userService.getUsersByFilter(firstName,lastName));
    }

    @GetMapping("/manager/users/{id}")
    public ResponseEntity <User> getUserById(@PathVariable Integer id) {
        LOGGER.info("Controller method called to view User by id="+id);
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @PostMapping("/manager/users")
    public ResponseEntity <User> createUser(@RequestBody UserDTO userDto) {
        LOGGER.info("Controller method called to create User;" +
                " user="+ userDto.toString());

        userService.validateUsername(userDto.getUserName());
        return ResponseEntity.ok().body(this.userService.createUser(userDto));
    }

    @PutMapping("/manager/users/{id}")
    public ResponseEntity <User> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDto) {
        userDto.setUserId(id);
        LOGGER.info("Controller method called to update User;" +
                " id="+id+", user="+userDto.toString());
        return ResponseEntity.ok().body(this.userService.updateUser(userDto));
    }

    @DeleteMapping("/manager/users/{id}")
    public HttpStatus deleteUser(@PathVariable Integer id) {
        LOGGER.info("Controller method called to delete User by id="+id);
        this.userService.deleteUser(id);
        return HttpStatus.OK;
    }
}
