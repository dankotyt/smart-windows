package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.pin36bik.dto.UserDTOForAdmin;
import ru.pin36bik.entity.ArchivedUser;
import ru.pin36bik.exceptions.EmailBusyException;
import ru.pin36bik.exceptions.UserNotFoundException;
import ru.pin36bik.entity.User;
import ru.pin36bik.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pin36bik.repository.ArchivedUserRepository;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.utils.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ArchivedUserRepository archivedUserRepository;
    private final UserMapper userMapper;
    //private final EmailService emailService // для восстановления пароля

    @Transactional(readOnly = true)
    public List<UserDTOForAdmin> getAllUsersDTOs() {
        List<User> users = userRepository.findAll();
        List<UserDTOForAdmin> userDTOs = users.stream()
                .map(userMapper :: toDTOForAdmin)
                .collect(Collectors.toList());
        log.info("Содержимое users: {}", userDTOs);
        return userDTOs;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
    }

    public UserDTO updateCurrentUser(String email, UserDTO userDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getSurname() != null) {
            user.setSurname(userDTO.getSurname());
        }
        if (userDTO.getBirthday() != null) {
            user.setBirthday(userDTO.getBirthday());
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new EmailBusyException("Email уже используется!");
            }
            user.setEmail(userDTO.getEmail());
        }

        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    public void deleteAndArchiveUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        ArchivedUser au = new ArchivedUser();
        au.setName(user.getName());
        au.setSurname(user.getSurname());
        au.setBirthday(user.getBirthday());
        au.setEmail(user.getEmail());
        au.setCreatedAt(user.getCreatedAt());
        archivedUserRepository.save(au);
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден с таким email: " + email));
    }
}
