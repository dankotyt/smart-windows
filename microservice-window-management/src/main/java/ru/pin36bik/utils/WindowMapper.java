package ru.pin36bik.utils;

import org.springframework.stereotype.Component;
import ru.pin36bik.dto.WindowUserDTO;
import ru.pin36bik.entity.WindowUser;

@Component
public class WindowMapper {
    public WindowUserDTO convertToDTO(WindowUser windowUser) {
        WindowUserDTO windowDTO = new WindowUserDTO();
        windowDTO.setWindowId(windowUser.getWindowId());
        windowDTO.setUserId(windowUser.getUserId());
        windowDTO.setName(windowUser.getName());
        windowDTO.setPresets(windowUser.getPresets());
        return windowDTO;
    }
}
