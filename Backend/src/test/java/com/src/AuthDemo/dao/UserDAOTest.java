package com.src.AuthDemo.dao;

import com.src.AuthDemo.model.User;
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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value={"/migrateDB_before.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value={"/migrateDB_after.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void getUserByIdTest(){
         User userFromDAO = userDAO.findById(1).get();
         User userFromQuery = jdbcTemplate.queryForObject(
                 "SELECT * FROM AuthTest.User WHERE userId = ? ",
                 new Object[]{1},
                 new BeanPropertyRowMapper<User>(User.class));
         Assert.assertTrue(userFromDAO.equals(userFromQuery));
    }

    @Test
    public void getUserByUserNameTest(){
        User userFromDAO = userDAO.findByUsername("Kiselevich").get();
        User userFromQuery = jdbcTemplate.queryForObject(
                "SELECT * FROM AuthTest.User WHERE userName = ? ",
                new Object[]{"Kiselevich"},
                new BeanPropertyRowMapper<User>(User.class));
        Assert.assertTrue(userFromDAO.equals(userFromQuery));
    }

    @Test
    public void getUserByFilterTest(){
        List<User> usersFromDAO = userDAO.filterByFirstNameAndLastName("Alexander","Kiselevich");
        List<User> usersByFilterFromQuery = jdbcTemplate.query(
                "SELECT * FROM User " +
                        "WHERE firstName LIKE CONCAT('%', ? ,'%') AND lastName LIKE CONCAT('%', ? ,'%') " +
                        "OR firstName LIKE CONCAT('%', ? ,'%') AND lastName LIKE CONCAT('%', ? ,'%')",
                new Object[]{ "Alexander", "Kiselevich", "Kiselevich", "Alexander"},
                new BeanPropertyRowMapper<User>(User.class));
        Assert.assertTrue(usersFromDAO.containsAll(usersByFilterFromQuery));
    }

    @Test
    public void getUserWithRoleTest(){
        List<User> usersFromDAO = userDAO.findUsersWithRole(1);
        List<User> usersWithRoleFromQuery = jdbcTemplate.query(
                "SELECT a.* FROM AuthTest.User a LEFT JOIN AuthTest.UserRole b ON a.userId=b.userId WHERE roleId= ?",
                new Object[]{ 1 },
                new BeanPropertyRowMapper<User>(User.class));
        Assert.assertTrue(usersFromDAO.containsAll(usersWithRoleFromQuery));
    }

    @Test
    public void getAllUserTest(){
        List<User> usersFromDAO = StreamSupport.stream(userDAO.findAll().spliterator(),false).collect(Collectors.toList());
        List<User> usersFromQuery = jdbcTemplate.query(
                "SELECT * FROM AuthTest.User",
                new BeanPropertyRowMapper<User>(User.class));
        Assert.assertTrue(usersFromDAO.containsAll(usersFromQuery));
    }

    @Test
    public void createUserTest(){
        User user= new User(
                null,
                "Ivan",
                "Ivan",
                "Ivanov",
                "Ivanov@gmail.com",
                "password",
                new Date(1L),
                true,
                new HashSet<>());
        userDAO.save(user);
        Assert.assertTrue(userDAO.findByUsername("Ivan").isPresent());
    }

    @Test
    public void updateUserTest(){
        User userBeforeUpdate = userDAO.findById(1).get();
        User userAfterUpdate = userDAO.findById(1).get();
        userAfterUpdate.setFirstName("Ivan");
        userDAO.save(userAfterUpdate);
        userAfterUpdate = jdbcTemplate.queryForObject(
                "SELECT * FROM AuthTest.User WHERE userId = ? ",
                new Object[]{userBeforeUpdate.getUserId()},
                new BeanPropertyRowMapper<User>(User.class));
        Assert.assertFalse(userAfterUpdate.equals(userBeforeUpdate));
    }

    @Test
    public void deleteUserByIdTest(){
        roleDAO.deleteAll();
        userDAO.deleteById(1);
        Assert.assertFalse(
                StreamSupport
                        .stream(userDAO.findAll().spliterator(),false)
                        .anyMatch(user -> user.getUserId().equals(1)));
    }

    @Test
    public void deleteAllUsersTest(){
        roleDAO.deleteAll();
        userDAO.deleteAll();
        List<User> allUsersFromQuery = jdbcTemplate.query(
                "SELECT * FROM AuthTest.User",
                new BeanPropertyRowMapper<User>(User.class));
        Assert.assertTrue(allUsersFromQuery.size()==0);
    }

}
