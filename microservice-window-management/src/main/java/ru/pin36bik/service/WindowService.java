package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.pin36bik.dto.*;
import org.springframework.stereotype.Service;
import ru.pin36bik.entity.WindowLocation;
import ru.pin36bik.entity.WindowUser;
import ru.pin36bik.exceptions.InvalidUserException;
import ru.pin36bik.exceptions.InvalidWindowIdException;
import ru.pin36bik.exceptions.NonUniqueWindowException;
import ru.pin36bik.repository.WindowRepository;
import ru.pin36bik.utils.WindowMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WindowService {
    private final WindowRepository windowRepository;
    private final WindowMapper windowMapper;

    public List<WindowUserDTO> findByUserEmail(String userEmail) {
        return windowRepository.findWindowsByUserEmail(userEmail).stream()
                .map(windowMapper::convertToDTO)
                .toList();
    }

    public WindowResponse addWindow(WindowRequest request, String userEmail) {

        if (windowRepository.existsByUserEmailAndWindowId(userEmail, request.getWindowId())) {
            throw new NonUniqueWindowException("Окно с ID " + request.getWindowId() +
                    " уже существует у пользователя " + userEmail);
        }

        var windowUser = new WindowUser();
        windowUser.setWindowId(request.getWindowId());
        windowUser.setUserEmail(userEmail);
        windowUser.setName(request.getName());
        windowUser.setStatus(true);
        windowUser.setLocation(new WindowLocation("Amsterdam", 52.37125, 4.89388));

        WindowUser saved = windowRepository.save(windowUser);
        return windowMapper.toResponse(saved);
    }

    public WindowResponse updateWindow(WindowRequestForUpdate request, String userEmail, Long windowId) {
        var windowUser = windowRepository.findByWindowId(windowId)
                .orElseThrow(() -> new InvalidWindowIdException("Окно с ID " + windowId + " не найдено"));

        if (!windowUser.getUserEmail().equals(userEmail)) {
            throw new InvalidUserException("Окно не принадлежит пользователю " + userEmail);
        }
        windowUser.setName(request.getName());
        WindowUser saved = windowRepository.save(windowUser);
        return windowMapper.toResponse(saved);
    }

    @Transactional
    public void deleteWindow(Long windowId, String userEmail) {
        var windowUser = windowRepository.findByWindowId(windowId)
                .orElseThrow(() -> new InvalidWindowIdException("Окно с ID " + windowId + " не найдено"));

        if (!windowUser.getUserEmail().equals(userEmail)) {
            throw new InvalidUserException("Email пользователя не совпадает с email владельца окна!");
        }

        windowRepository.delete(windowUser);
    }

    public WindowLocationDTO getLocation(Long windowId, String userEmail) {
        var windowUser = windowRepository.findByWindowId(windowId)
                .orElseThrow(() -> new InvalidWindowIdException("Окно с ID " + windowId + " не найдено"));
        if (!windowUser.getUserEmail().equals(userEmail)) {
            throw new InvalidUserException("Email пользователя не совпадает с email владельца окна!");
        }
        return windowMapper.toLocationDTO(windowUser);
    }
}
