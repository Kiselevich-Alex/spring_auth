package com.src.AuthDemo.security.jwt;

import com.src.AuthDemo.model.Role;
import com.src.AuthDemo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory(){
    }

    public static JwtUser create(User user){
        return new JwtUser(
                (long)user.getUserId(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRoles()),
                user.isEnabled(),
                user.getLastPasswordResetDate()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Set<Role> userRoles){
        List<Role> RoleList = userRoles.stream().collect(Collectors.toList());

        return RoleList.stream()
                .map(role->
                        new SimpleGrantedAuthority("ROLE_"+role.getRoleName())
                ).collect(Collectors.toList());
    }
}
