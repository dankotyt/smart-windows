package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import ru.pin36bik.dto.WindowRequest;
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

    public List<WindowUserDTO> findByUserEmail(String userEmail) {
        return windowRepository.findByUserEmail(userEmail).stream()
                .map(windowMapper::convertToDTO)
                .toList();
    }

    public WindowResponse addWindow(WindowRequest request, String userEmail) {
        WindowUser windowUser = new WindowUser();
        windowUser.setWindowId(request.getWindowId());
        windowUser.setUserEmail(userEmail);
        windowUser.setName(request.getName());
        windowUser.setStatus(true);

        WindowUser saved = windowRepository.save(windowUser);
        return windowMapper.toResponse(saved);
    }
}
