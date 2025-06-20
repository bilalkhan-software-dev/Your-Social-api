package com.yoursocial.dto;


import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {


    private Integer id ;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;

    private List<Integer> followers = new ArrayList<>();
    private List<Integer> following = new ArrayList<>();

    private List<PostResponse> savedPost = new ArrayList<>();


}
