package com.yoursocial.dto;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {


    private Integer id ;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;

    private List<Integer> followers = new ArrayList<>();
    private List<Integer> following = new ArrayList<>();

}
