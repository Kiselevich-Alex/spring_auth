package com.src.AuthDemo.service;

import com.src.AuthDemo.model.Role;

import java.util.List;

public interface RoleService {
    void createRole(Role role);
    Role getRoleById(Integer roleId);
    List<Role> getAllRoles();
    Role updateRole(Role role);
    void deleteRole(Integer roleId);
}
