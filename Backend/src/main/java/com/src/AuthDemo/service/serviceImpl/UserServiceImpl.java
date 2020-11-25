package com.src.AuthDemo.service.serviceImpl;

import com.src.AuthDemo.dao.RoleDAO;
import com.src.AuthDemo.dao.UserDAO;
import com.src.AuthDemo.model.Role;
import com.src.AuthDemo.model.User;
import com.src.AuthDemo.model.dto.UserDTO;
import com.src.AuthDemo.service.UserService;
import com.src.AuthDemo.util.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    private UserDAO userDao;

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public List<User> getAllUsers() {
        LOGGER.info("Service method called to view all list of Users");
        List<User> users = StreamSupport.stream(userDao.findAll().spliterator(),false).collect(Collectors.toList());
        LOGGER.info("Records were found: "+users.size());
        return users;
    }

    @Override
    public List<User> getUsersByFilter(String firstName, String lastName) {
        if(firstName==null)
            firstName="";
        if (lastName==null)
            lastName="";
        LOGGER.info("Service method called to view all Users by Filter");
        List<User> usersByFilter = userDao.filterByFirstNameAndLastName(firstName,lastName);

        String finalFirstName = firstName;
        String finalLastName = lastName;

        if(usersByFilter.stream().anyMatch(user ->
                        (user.getFirstName() == finalFirstName || user.getFirstName() == finalLastName) &&
                        (user.getLastName() == finalFirstName || user.getLastName() == finalLastName))) {
            usersByFilter.removeIf(user ->
                    (user.getFirstName() != finalFirstName && user.getFirstName() != finalLastName) ||
                    (user.getLastName() != finalFirstName && user.getLastName() != finalLastName));
        }
        LOGGER.info("Records found: "+usersByFilter.size());
        return usersByFilter;
    }

    @Override
    public User getUserById(Integer userId) {
        LOGGER.info("Service method called to view User by id= "+userId);
        Optional<User> userOptional = userDao.findById(userId);
        if(userOptional.isPresent()) {
            LOGGER.info("User was found: " +userOptional.get().toString());
            return userOptional.get();
        } else {
            LOGGER.info("User was not found");
            LOGGER.error("User was not found");
            throw new ResourceNotFoundException("No user record exist for given id");
        }
    }

    @Override
    public User getUserByName(String userName) {
        LOGGER.info("Service method called to view User by UserName= "+userName);
        Optional<User> userOptional = userDao.findByUsername(userName);
        if(userOptional.isPresent()) {
            LOGGER.info("User was found: " +userOptional.get().toString());
            return userOptional.get();
        } else {
            LOGGER.info("User was not found");
            LOGGER.error("User was not found");
            throw new ResourceNotFoundException("No user record exist for given UserName");
        }
    }

    @Override
    public User createUser(UserDTO userDto) {
        LOGGER.info("Service method called to create User: "+userDto.toString());
        User user = new User(
                null,
                userDto.getUserName(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                bCryptPasswordEncoder.encode(userDto.getPassword()),
                new Date(),
                true,
                new HashSet<>(Arrays.asList(roleDAO.findById(1).get())));
        User savedUser= userDao.save(user);
        Role usersRole= roleDAO.findById(1).get();
        usersRole.getUser().add(user);
        roleDAO.save(usersRole);
        return savedUser;
    }

    @Override
    public User updateUser(UserDTO userDto) {
        LOGGER.info("Service method called to update User: "+userDto.toString());
        Optional<User> user = userDao.findById(userDto.getUserId());
        if(user.isPresent()){
            LOGGER.info("User was found");
            List<Role> oldUserRoles= user.get().getRoles().stream().collect(Collectors.toList());
            oldUserRoles.forEach(role->{
                Set<User> newUserSet = role.getUser().stream().filter(oldUser -> oldUser.getUserId() != userDto.getUserId()).collect(Collectors.toSet());
                role.setUser(newUserSet);
                roleDAO.save(role);
            });
            List<Role> newUserRoles = new ArrayList<>();
            userDto.getSetOfRoleId().forEach(roleId->{
                newUserRoles.add(roleDAO.findById(roleId).get());
            });
            user.get().setUserName(userDto.getUserName());
            user.get().setFirstName(userDto.getFirstName());
            user.get().setLastName(userDto.getLastName());
            user.get().setEmail(userDto.getEmail());
            user.get().setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            user.get().setLastPasswordResetDate(userDto.getLastPasswordResetDate());
            user.get().setEnabled(userDto.isEnabled());
            user.get().setRoles(newUserRoles.stream().collect(Collectors.toSet()));
            newUserRoles.forEach( role -> {
                role.getUser().add(user.get());
                roleDAO.save(role);
            });
            return userDao.save(user.get());
        } else {
            LOGGER.info("User was not found");
            LOGGER.error("User was not found");
            throw new ResourceNotFoundException("No user record exist for given userId");
        }
    }

    @Override
    public boolean validateUsername(String username) {
        Optional<User> user= userDao.findByUsername(username);
        if (user.isPresent()) {
            throw new ResourceNotFoundException("User with this username exists");
        } else {
            return true;
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        LOGGER.info("Service method called to delete User by userId, userId= " + userId);
        Optional<User> user = userDao.findById(userId);
        if (user.isPresent()) {
            LOGGER.info("User was found: " + user.get().toString());
            List<Role> roles = user.get().getRoles().stream().collect(Collectors.toList());
            roles.forEach(role -> {
                role.getUser().remove(user.get());
                roleDAO.save(role);
            });
            userDao.delete(user.get());
        } else {
            LOGGER.info("User was not found");
            LOGGER.error("User was not found");
            throw new ResourceNotFoundException("No user record exist for given userId");
        }
    }
}
