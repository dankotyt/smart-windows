package ru.pin36bik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.pin36bik.config.JwtConfig;
import ru.pin36bik.dto.LoginRequest;
import ru.pin36bik.dto.RegistrationRequest;
import ru.pin36bik.entity.ArchivedUser;
import ru.pin36bik.exceptions.IncorrectPasswordException;
import ru.pin36bik.exceptions.EmailBusyException;
import ru.pin36bik.exceptions.UserNotFoundException;
import ru.pin36bik.entity.User;
import ru.pin36bik.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pin36bik.repository.ArchivedUserRepository;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.security.jwt.JwtTokenFactory;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ArchivedUserRepository archivedUserRepository;
    private final PasswordEncoder passwordEncoder;
    //private final EmailService emailService // для восстановления пароля

    public UserDTO registerUser(RegistrationRequest registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new EmailBusyException("The user has already existed with this email!");
        }

        User user = new User();
        user.setName(registrationDTO.getName());
        user.setLastName(registrationDTO.getLastName());
        user.setBirthday(registrationDTO.getBirthday());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        userRepository.save(user);

        return convertToDTO(user);
    }

    public UserDTO authorizeUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Неверный пароль");
        }
        return convertToDTO(user);
    }

    //понадобиться для микросервиса аналитики
    public List<User> getAllUsers() {
        return userRepository.findAll();
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
        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
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
        return convertToDTO(user);
    }

    @Transactional
    public void deleteAndArchiveUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        ArchivedUser au = new ArchivedUser();
        au.setName(user.getName());
        au.setLastName(user.getLastName());
        au.setBirthday(user.getBirthday());
        au.setEmail(user.getEmail());
        au.setCreatedAt(user.getCreatedAt());
        archivedUserRepository.save(au);
        userRepository.delete(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден с таким email: " + email));
    }
}
