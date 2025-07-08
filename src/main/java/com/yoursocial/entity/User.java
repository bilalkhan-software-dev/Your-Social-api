package com.yoursocial.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.access.method.P;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column(length = 100)
    private String bio;

    @Column(length = 400)
    private String image;

    @Column(length = 400)
    private String banner;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> followers = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> following = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER)
    private List<Post> savedPost = new ArrayList<>();

    // Helper method
    public List<Integer> getSavedPostIds() {
        return savedPost.stream().map(Post::getId).collect(Collectors.toList());
    }
}



