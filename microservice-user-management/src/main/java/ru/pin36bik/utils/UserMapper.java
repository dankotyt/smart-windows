package ru.pin36bik.utils;

import org.springframework.stereotype.Component;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.entity.User;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
