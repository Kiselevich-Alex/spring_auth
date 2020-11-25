package com.src.AuthDemo.dao;

import com.src.AuthDemo.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends CrudRepository<User, Integer> {
    @Query(value="SELECT * FROM User WHERE userName= :userName", nativeQuery = true)
    Optional<User> findByUsername(@Param("userName") String userName);

    @Query(value="SELECT * FROM User " +
            "WHERE firstName LIKE CONCAT('%',:firstName,'%') AND lastName LIKE CONCAT('%',:lastName,'%') " +
            "OR firstName LIKE CONCAT('%',:lastName,'%') AND lastName LIKE CONCAT('%',:firstName,'%')",
            nativeQuery = true)
    List<User> filterByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query(value="SELECT a.* FROM User a LEFT JOIN UserRole b ON a.userId=b.userId "+
            "WHERE roleId= :roleId", nativeQuery = true)
    List<User> findUsersWithRole(@Param("roleId") Integer roleId);
}
