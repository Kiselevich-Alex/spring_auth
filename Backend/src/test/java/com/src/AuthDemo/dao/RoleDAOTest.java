package com.src.AuthDemo.dao;

import com.src.AuthDemo.model.Role;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value={"/migrateDB_before.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value={"/migrateDB_after.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleDAOTest {

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void getRoleByIdTest(){
        Role roleFromDAO = roleDAO.findById(1).get();
        Role roleFromQuery = jdbcTemplate.queryForObject(
                "SELECT * FROM AuthTest.Role WHERE roleId = ? ",
                new Object[]{1},
                new BeanPropertyRowMapper<Role>(Role.class));
        Assert.assertTrue(roleFromDAO.equals(roleFromQuery));
    }

    @Test
    public void getAllRolesTest(){
        List<Role> allRolesFromDAO = StreamSupport.stream(roleDAO.findAll().spliterator(),false).collect(Collectors.toList());
        List<Role> allRolesFromQuery = jdbcTemplate.query(
                "SELECT * FROM AuthTest.Role",
                new BeanPropertyRowMapper<Role>(Role.class));
        Assert.assertTrue(allRolesFromDAO.containsAll(allRolesFromQuery));
    }

    @Test
    public void createRoleTest(){
        Role role = new Role(null,"DIRECTOR");
        roleDAO.save(role);
        Assert.assertTrue(
                StreamSupport
                        .stream(roleDAO.findAll().spliterator(),false)
                        .anyMatch(role1 -> role1.getRoleName().equals("DIRECTOR")));
    }

    @Test
    public void updateRoleTest(){
        Role roleBeforeUpdate = roleDAO.findById(1).get();
        Role roleAfterUpdate = roleDAO.findById(1).get();
        roleAfterUpdate.setRoleName("DIRECTOR");
        roleDAO.save(roleAfterUpdate);
        roleAfterUpdate = jdbcTemplate.queryForObject(
                "SELECT * FROM AuthTest.Role WHERE roleId = ? ",
                new Object[]{roleBeforeUpdate.getRoleId()},
                new BeanPropertyRowMapper<Role>(Role.class));
        Assert.assertFalse(roleAfterUpdate.equals(roleBeforeUpdate));
    }

    @Test
    public void deleteRoleTest(){
        roleDAO.deleteById(1);
        List<Role> allRolesFromQuery = jdbcTemplate.query(
                "SELECT * FROM AuthTest.Role",
                new BeanPropertyRowMapper<Role>(Role.class));
        Assert.assertFalse(allRolesFromQuery.stream().filter(role-> role.getRoleId()==1).findAny().isPresent());
    }

    @Test
    public void deleteAllRolesTest(){
        roleDAO.deleteAll();
        List<Role> allRolesFromQuery = jdbcTemplate.query(
                "SELECT * FROM AuthTest.Role",
                new BeanPropertyRowMapper<Role>(Role.class));
        Assert.assertTrue(allRolesFromQuery.size()==0);
    }
}
