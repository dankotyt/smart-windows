package ru.pin36bik.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.dto.UserDTOForAdmin;
import ru.pin36bik.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.pin36bik.service.CookieService;
import ru.pin36bik.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CookieService cookieService;

    @GetMapping("/admin/get_users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTOForAdmin>> getAllUsersByAdmin() {
        return ResponseEntity.ok(userService.getAllUsersDTOs());
    }

    @GetMapping("/admin/get_by_id/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/admin/get_by_email/{email}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()") //"#userDTO.email == authentication.name or hasAuthority('ROLE_ADMIN')"
    public ResponseEntity<UserDTO> updateCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateCurrentUser(userDetails.getUsername(), userDTO));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("#email == authentication.name or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAndArchiveCurrentUser(
            @RequestParam String email,
            HttpServletResponse response) {
        userService.deleteAndArchiveUser(email);
        cookieService.expireAllCookies(response);
        return ResponseEntity.noContent().build();
    }
}
