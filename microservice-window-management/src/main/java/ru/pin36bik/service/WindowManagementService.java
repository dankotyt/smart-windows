package ru.pin36bik.service;

import ru.pin36bik.model.WindowManagementDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.repository.WindowManagementRepository;

@Service
public class WindowManagementService {
    private final WindowManagementRepository windowManagementRepository;
    private final UserRepository userRepository;

    @Autowired
    public WindowManagementService(WindowManagementRepository windowManagementRepository,
                                   UserRepository userRepository) {
        this.windowManagementRepository = windowManagementRepository;
        this.userRepository = userRepository;
    }

    public WindowManagementDTO addWindowToUser(String windowId, String userId, String name,
                                               String location) {

    }
}
