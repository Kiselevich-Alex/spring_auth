package com.src.AuthDemo.model;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleTest {

    @Test
    public void createRoleTest(){
        Role role = new Role(1,"USER");
        Assert.assertNotNull(role);
    }

    @Test
    public void roleToStringTest(){
        Role role = new Role(1,"USER");
        Assert.assertEquals("Role{roleId=1, roleName='USER'}",role.toString());
    }

    @Test
    public void correctEqualsRoleTest(){
        Role firstRoleToCompare = new Role(1,"USER");
        Role secondRoleToCompare = new Role(1,"USER");
        Assert.assertTrue(firstRoleToCompare.equals(secondRoleToCompare));
    }

    @Test
    public void incorrectEqualsRoleTest(){
        Role firstRoleToCompare = new Role(1,"USER");
        Role secondRoleToCompare = new Role(2,"ADMIN");
        Assert.assertFalse(firstRoleToCompare.equals(secondRoleToCompare));
    }
}
