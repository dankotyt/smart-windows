package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.pin36bik.dto.WindowRequest;
import ru.pin36bik.dto.WindowResponse;
import ru.pin36bik.dto.WindowUserDTO;
import org.springframework.stereotype.Service;
import ru.pin36bik.entity.WindowUser;
import ru.pin36bik.exceptions.InvalidUserException;
import ru.pin36bik.exceptions.InvalidWindowIdException;
import ru.pin36bik.exceptions.NonUniqueWindowException;
import ru.pin36bik.repository.WindowRepository;
import ru.pin36bik.utils.WindowMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WindowService {
    private final WindowRepository windowRepository;
    private final WindowMapper windowMapper;

    public List<WindowUserDTO> findByUserEmail(String userEmail) {
        return windowRepository.findWindowsByUserEmail(userEmail).stream()
                .map(windowMapper::convertToDTO)
                .toList();
    }

    //пофиксить (exception не тот + не работает)
    public WindowResponse addWindow(WindowRequest request, String userEmail) {

        windowRepository.findByWindowId(request.getWindowId())
                .ifPresent(w -> {
                    throw new NonUniqueWindowException("Окно с таким ID уже есть в базе!");
                });

        WindowUser windowUser = new WindowUser();
        windowUser.setWindowId(request.getWindowId());
        windowUser.setUserEmail(userEmail);
        windowUser.setName(request.getName());
        windowUser.setStatus(true);

        WindowUser saved = windowRepository.save(windowUser);
        return windowMapper.toResponse(saved);
    }

    //пофиксить (exception не тот)
    public WindowResponse updateWindow(WindowRequest request, String userEmail, Long windowId) {
        WindowUser windowUser = windowRepository.findByWindowId(windowId)
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
        WindowUser windowUser = windowRepository.findByWindowId(windowId)
                .orElseThrow(() -> new InvalidWindowIdException("Окно с ID " + windowId + " не найдено"));

        if (!windowUser.getUserEmail().equals(userEmail)) {
            throw new InvalidUserException("Email пользователя не совпадает с email владельца окна!");
        }

        windowRepository.delete(windowUser);
    }
}
