package com.jala.university.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private String id;
    private String name;
    private String login;
    private String password;

    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}