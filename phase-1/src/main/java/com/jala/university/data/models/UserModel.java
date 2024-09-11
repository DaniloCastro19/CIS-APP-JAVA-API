package com.jala.university.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Name is required")
    @Size(max = 30, message = "Name cannot be more than 30 characters")
    private String name;

    @NotBlank(message = "Login is required")
    @Size(max = 16, message = "Login cannot be more than 16 characters")
    @Column(unique = true)
    private String login;

    @NotBlank(message = "Password is required")
    private String password;
}