package com.yoursocial.repository;

import com.yoursocial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);


    @Query("select u from User u where (lower(u.firstName)  like lower(concat( '%',:query,'%') ))" +
            " or u.lastName like  %:query%" +
            " or u.email like %:query%")
    List<User> searchUser(@Param("query") String query);
}
