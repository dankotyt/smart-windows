package ru.pin36bik.utils;

import org.springframework.stereotype.Component;
import ru.pin36bik.dto.WindowResponse;
import ru.pin36bik.dto.WindowUserDTO;
import ru.pin36bik.entity.WindowUser;

@Component
public class WindowMapper {
    public WindowUserDTO convertToDTO(WindowUser windowUser) {
        WindowUserDTO windowDTO = new WindowUserDTO();
        windowDTO.setWindowId(windowUser.getWindowId());
        windowDTO.setUserEmail(windowUser.getUserEmail());
        windowDTO.setName(windowUser.getName());
        windowDTO.setPresets(windowUser.getPresets());
        return windowDTO;
    }

    public WindowResponse toResponse(WindowUser windowUser) {
        return new WindowResponse(
                windowUser.getWindowId(),
                windowUser.getUserEmail(),
                windowUser.getName(),
                windowUser.isStatus(),
                windowUser.getPresets()
        );
    }
}
