package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import ru.pin36bik.dto.WindowCreateRequest;
import ru.pin36bik.dto.WindowResponse;
import ru.pin36bik.dto.WindowUserDTO;
import org.springframework.stereotype.Service;
import ru.pin36bik.entity.WindowUser;
import ru.pin36bik.repository.WindowRepository;
import ru.pin36bik.utils.WindowMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WindowService {
    private final WindowRepository windowRepository;
    private final WindowMapper windowMapper;

    public List<WindowUserDTO> findByUserId(Long userId) {
        return windowRepository.findByUserId(userId).stream()
                .map(windowMapper::convertToDTO)
                .toList();
    }

    public WindowResponse addWindow(WindowCreateRequest request) {
        WindowUser windowUser = new WindowUser();
        windowUser.setWindowId(request.getWindowId());
        windowUser.setUserId(request.getUserId());
        windowUser.setName(request.getName());
        windowUser.setStatus(true);

        WindowUser saved = windowRepository.save(windowUser);
        return new WindowResponse(
                saved.getWindowId(),
                saved.getUserId(),
                saved.getName(),
                saved.isStatus(),
                saved.getPresets()
        );
    }
}
