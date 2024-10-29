package com.jala.university.api.utils;

import com.jala.university.core.utils.UserMapper;
import com.jala.university.data.dto.UserDTO;
import com.jala.university.data.models.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        userMapper = new UserMapper(modelMapper);
    }

    @Test
    void shouldMapUserModelToDTO() {
        UserModel userModel = new UserModel();
        userModel.setId(String.valueOf(1L));
        userModel.setName("John Doe");
        userModel.setLogin("john@example.com");

        UserDTO userDTO = userMapper.toDTO(userModel);

        assertEquals(userModel.getId(), userDTO.getId());
        assertEquals(userModel.getName(), userDTO.getName());
        assertEquals(userModel.getLogin(), userDTO.getLogin());
    }

    @Test
    void shouldMapUserDTOToModel() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(String.valueOf(1L));
        userDTO.setName("Jane Doe");
        userDTO.setLogin("jane@example.com");

        UserModel userModel = userMapper.toModel(userDTO);

        assertEquals(userDTO.getId(), userModel.getId());
        assertEquals(userDTO.getName(), userModel.getName());
        assertEquals(userDTO.getLogin(), userModel.getLogin());
    }
}