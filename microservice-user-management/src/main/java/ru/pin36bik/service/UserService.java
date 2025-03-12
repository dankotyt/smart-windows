package ru.pin36bik.service;

import ru.pin36bik.dto.UserLoginDTO;
import ru.pin36bik.dto.UserRegistrationDTO;
import ru.pin36bik.entity.ArchivedUser;
import ru.pin36bik.exceptions.IncorrectPasswordException;
import ru.pin36bik.exceptions.EmailBusyException;
import ru.pin36bik.exceptions.UserNotFoundException;
import ru.pin36bik.entity.User;
import ru.pin36bik.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pin36bik.repository.ArchivedUserRepository;
import ru.pin36bik.repository.UserRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ArchivedUserRepository archivedUserRepository;
    private final PasswordEncoder passwordEncoder;
    //private final EmailService emailService // для восстановления пароля

    @Autowired
    public UserService(UserRepository userRepository, ArchivedUserRepository archivedUserRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.archivedUserRepository = archivedUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new EmailBusyException("The user has already existed with this email!");
        }

        User user = new User();
        user.setName(registrationDTO.getName());
        user.setLastName(registrationDTO.getLastName());
        user.setBirthday(registrationDTO.getBirthday());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now(Clock.systemUTC()));

        userRepository.save(user);

        return convertToDTO(user);
    }

    public UserDTO authorizeUser(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password!");
        }
        return convertToDTO(user);
    }

    //понадобиться для микросервиса аналитики
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO updateCurrentUser(String email, UserDTO userDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

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
                throw new EmailBusyException("Email is already in use!");
            }
            user.setEmail(userDTO.getEmail());
        }

        userRepository.save(user);
        return convertToDTO(user);
    }

    public void deleteAndArchiveUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        ArchivedUser au = new ArchivedUser();
        au.setName(user.getName());
        au.setLastName(user.getLastName());
        au.setBirthday(user.getBirthday());
        au.setEmail(user.getEmail());
        au.setCreatedAt(user.getCreatedAt());
        au.setDeletedAt(LocalDateTime.now(Clock.systemUTC()));
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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return user;
    }


}
