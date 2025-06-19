package com.yoursocial.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
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

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> followers = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> following = new ArrayList<>();

}
