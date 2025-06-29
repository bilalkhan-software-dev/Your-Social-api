package com.yoursocial.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.access.method.P;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    private String password;
    private String gender;


    @ElementCollection
    private List<Integer> followers = new ArrayList<>();

    @ElementCollection
    private List<Integer> following = new ArrayList<>();


    @JsonIgnore
    @ManyToMany
    private List<Post> savedPost = new ArrayList<>();
}



