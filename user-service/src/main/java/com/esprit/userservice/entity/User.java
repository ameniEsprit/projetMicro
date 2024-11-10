package com.esprit.userservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String photoprofile;
    private String phone;
    @JsonIgnore
    private String password;
    @ManyToMany
    @JsonIgnore
    private List<Role> roles;
}

