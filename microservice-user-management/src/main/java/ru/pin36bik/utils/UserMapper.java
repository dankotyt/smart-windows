package ru.pin36bik.utils;

import org.springframework.stereotype.Component;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.dto.UserDTOForAdmin;
import ru.pin36bik.entity.User;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setBirthday(user.getBirthday());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public UserDTOForAdmin toDTOForAdmin(User user) {
        UserDTOForAdmin dto = new UserDTOForAdmin();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setBirthday(user.getBirthday());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setRole(user.getRole());
        return dto;
    }
}
