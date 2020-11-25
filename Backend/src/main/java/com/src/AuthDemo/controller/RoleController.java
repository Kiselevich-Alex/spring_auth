package com.src.AuthDemo.controller;


import com.src.AuthDemo.model.Role;
import com.src.AuthDemo.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
public class RoleController {

    private static final Logger LOGGER = LogManager.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @GetMapping("/admin/roles")
    public ResponseEntity<List<Role>> getAllUsers() {
        LOGGER.info("Controller method called to view all list of Roles");
        return ResponseEntity.ok().body(roleService.getAllRoles());
    }

    @GetMapping("/admin/roles/{id}")
    public ResponseEntity <Role> getRoleById(@PathVariable Integer id) {
        LOGGER.info("Controller method called to view Role by id="+id);
        return ResponseEntity.ok().body(roleService.getRoleById(id));
    }

    @PostMapping("/admin/roles")
    public HttpStatus createRole(@RequestBody Role role) {
        LOGGER.info("Controller method called to create Role;" +
                " Role="+ role.toString());
        roleService.createRole(role);
        return HttpStatus.OK;
    }

    @PutMapping("/admin/roles/{id}")
    public ResponseEntity <Role> updateRole(@PathVariable Integer id, @RequestBody Role role) {
        role.setRoleId(id);
        LOGGER.info("Controller method called to update User;" +
                " id="+id+", user="+role.toString());
        return ResponseEntity.ok().body(roleService.updateRole(role));
    }

    @DeleteMapping("/admin/roles/{id}")
    public HttpStatus deleteRole(@PathVariable Integer id) {
        LOGGER.info("Controller method called to delete Role by id="+id);
        roleService.deleteRole(id);
        return HttpStatus.OK;
    }
}
