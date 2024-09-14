package com.jala.university.core.utils;

import com.jala.university.data.dto.UserDTO;
import com.jala.university.data.models.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO toDTO(UserModel user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public UserModel toModel(UserDTO dto) {
        return modelMapper.map(dto, UserModel.class);
    }
}