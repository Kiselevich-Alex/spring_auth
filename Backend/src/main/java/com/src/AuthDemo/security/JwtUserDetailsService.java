package com.src.AuthDemo.security;

import com.src.AuthDemo.model.User;
import com.src.AuthDemo.security.jwt.JwtUser;
import com.src.AuthDemo.security.jwt.JwtUserFactory;
import com.src.AuthDemo.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LogManager.getLogger(JwtUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user=userService.getUserByName(userName);
        if(user!=null){
            JwtUser jwtUser= JwtUserFactory.create(user);
            LOGGER.info("In loadUserByUsername; user with userName: "+ userName+" successfully loaded");
            return jwtUser;
        } else {
            throw new UsernameNotFoundException("User with Name="+userName+" not found");
        }
    }

}
