package com.src.AuthDemo.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Test
    public void createUserTest(){
        User user= new User(
                1,
                "Ivan",
                "Ivan",
                "Ivanov",
                "Ivanov@gmail.com",
                "password",
                new Date(1L),
                true,
                new HashSet<>());
        Assert.assertNotNull(user);
    }

    @Test
    public void userToStringTest(){
        User user= new User(
                1,
                "Ivan",
                "Ivan",
                "Ivanov",
                "Ivanov@gmail.com",
                "password",
                new Date(1L),
                true,
                new HashSet<>());
        Assert.assertEquals("User{userId=1, userName='Ivan', firstName='Ivan', lastName='Ivanov', email='Ivanov@gmail.com', password='password', lastPasswordResetDate=Thu Jan 01 03:00:00 MSK 1970, enabled=true}",user.toString());
    }

    @Test
    public void correctEqualsUserTest(){
        User firstUserToCompare= new User(
                1,
                "Ivan",
                "Ivan",
                "Ivanov",
                "Ivanov@gmail.com",
                "password",
                new Date(1L),
                true,
                new HashSet<>());
        User secondUserToCompare= new User(
                1,
                "Ivan",
                "Ivan",
                "Ivanov",
                "Ivanov@gmail.com",
                "password",
                new Date(1L),
                true,
                new HashSet<>());
        Assert.assertTrue(firstUserToCompare.equals(secondUserToCompare));
    }

    @Test
    public void incorrectEqualsUserTest(){
        User firstUserToCompare= new User(
                1,
                "Ivan",
                "Ivan",
                "Ivanov",
                "Ivanov@gmail.com",
                "password",
                new Date(1L),
                true,
                new HashSet<>());
        User secondUserToCompare= new User(
                1,
                "Peter",
                "Peter",
                "Ivanov",
                "Ivanov@gmail.com",
                "password",
                new Date(1L),
                true,
                new HashSet<>());
        Assert.assertFalse(firstUserToCompare.equals(secondUserToCompare));
    }

}
