package com.yoursocial.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    private String caption;

    private String image;

    private String video;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private User user;

    @OneToMany
    private List<User> like = new ArrayList<>();

    private LocalDateTime createdAt;



}
