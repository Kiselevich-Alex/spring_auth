package com.src.AuthDemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDTO {
    private Integer userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date lastPasswordResetDate;
    private boolean enabled;
    private Set<Integer> setOfRoleId;
}
