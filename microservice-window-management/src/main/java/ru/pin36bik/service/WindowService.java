package ru.pin36bik.service;

import ru.pin36bik.dto.WindowUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.repository.WindowRepository;

@Service
public class WindowService {
    private final WindowRepository windowManagementRepository;
    private final UserRepository userRepository;

    @Autowired
    public WindowService(WindowRepository windowManagementRepository,
                         UserRepository userRepository) {
        this.windowManagementRepository = windowManagementRepository;
        this.userRepository = userRepository;
    }

//    public WindowUserDTO addWindowToUser(String windowId, String userId, String name,
//                                         String location) {
//
//    }
}
