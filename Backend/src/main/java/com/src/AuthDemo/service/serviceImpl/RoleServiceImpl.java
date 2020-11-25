package com.src.AuthDemo.service.serviceImpl;

import com.src.AuthDemo.dao.RoleDAO;
import com.src.AuthDemo.dao.UserDAO;
import com.src.AuthDemo.model.Role;
import com.src.AuthDemo.model.User;
import com.src.AuthDemo.service.RoleService;
import com.src.AuthDemo.util.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LogManager.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public void createRole(Role role) {
        LOGGER.info("Service method called to create new role, "+role.toString());
        roleDAO.save(role);
        LOGGER.info("Record was saved");
    }

    @Override
    public Role getRoleById(Integer roleId) {
        LOGGER.info("Service method called to view role by Id, Id="+roleId);
        Optional<Role> role = roleDAO.findById(roleId);
        if(role.isPresent()){
            LOGGER.info("record is present: "+ role.get().toString());
            return role.get();
        } else {
            LOGGER.info("Role was not found");
            LOGGER.error("Role was not found");
            throw new ResourceNotFoundException("No role record exist for given id");
        }
    }

    @Override
    public List<Role> getAllRoles() {
        LOGGER.info("Service method called to view all roles");
        List<Role> roles = StreamSupport.stream(roleDAO.findAll().spliterator(),false).collect(Collectors.toList());
        if(roles.size()>0){
            LOGGER.info("Records were found:" + roles.size());
            return roles;
        } else {
            LOGGER.info("Roles was not found");
            LOGGER.error("Roles was not found");
            throw new ResourceNotFoundException("Roles not found");
        }
    }

    @Override
    public Role updateRole(Role role) {
        LOGGER.info("Service method called to update role, "+ role.toString());
        Optional<Role> roleFromDB=roleDAO.findById(role.getRoleId());
        if(roleFromDB.isPresent()){
          LOGGER.info("Role was found");
          roleFromDB.get().setRoleName(role.getRoleName());
          roleDAO.save(roleFromDB.get());
          LOGGER.info("Role was saved");
          return roleFromDB.get();
        } else {
            LOGGER.info("Roles was not found");
            LOGGER.error("Roles was not found");
            throw new ResourceNotFoundException("No role record exist for given id");
        }
    }

    @Override
    public void deleteRole(Integer roleId) {
        LOGGER.info("Service method called to delete role by Id, Id= "+roleId);
        List<User> usersWithRole= userDAO.findUsersWithRole(roleId);
        if(usersWithRole.size()>0){
            usersWithRole.forEach(user -> user.getRoles().removeIf(role -> role.getRoleId()==roleId));
            userDAO.saveAll(usersWithRole);
        } else {
            LOGGER.info("Users with this role aren't exist");
        }
        roleDAO.deleteById(roleId);
        LOGGER.info("Role was deleted");
    }
}
