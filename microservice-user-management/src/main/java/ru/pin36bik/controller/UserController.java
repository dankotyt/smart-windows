package ru.pin36bik.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.dto.UserDTOForAdmin;
import ru.pin36bik.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.pin36bik.repository.UserRepository;
import ru.pin36bik.service.CookieService;
import ru.pin36bik.service.UserService;
import ru.pin36bik.utils.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CookieService cookieService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @GetMapping("/admin/get_users")
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTOForAdmin>> getAllUsersByAdmin() {
        List<UserDTOForAdmin> users = userService.getAllUsersDTOs();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admin/get_by_id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/admin/get_by_email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("#userDTO.email == principal.username or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateCurrentUser(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateCurrentUser(userDetails.getUsername(), userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("#email == principal.username or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAndArchiveCurrentUser(
            @RequestParam String email,
            HttpServletResponse response) {
        userService.deleteAndArchiveUser(email);
        cookieService.expireAllCookies(response);
        return ResponseEntity.ok().build();
    }
}
