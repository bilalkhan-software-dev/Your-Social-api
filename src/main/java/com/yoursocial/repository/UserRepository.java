package com.yoursocial.repository;

import com.yoursocial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);


    @Query("select u from User u where lower(u.firstName) like lower(concat('%',:query,'%'))" +
            " or lower(u.lastName) like lower(concat('%',:query,'%'))" +
            " or lower(u.email) like lower(concat('%',:query,'%'))")
    List<User> searchUser(@Param("query") String query);

    boolean existsByEmail(String email);


}
