package com.jala.university.core.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, length = 36)
    private String id;

    @NotEmpty(message = "Name Can't be blank")
    @Column(nullable = false, length = 200)
    private String name;

    @NotEmpty(message = "Login Can't be blank")
    @Column(nullable = false, length = 20)
    private String login;

    @NotEmpty(message = "Password Can't be blank")
    @Column(nullable = false, length = 100)
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
