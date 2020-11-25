package com.src.AuthDemo.dao;

import com.src.AuthDemo.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleDAO extends CrudRepository<Role, Integer> {
}
