package ru.pin36bik.controller;

import ru.pin36bik.dto.UserDTO;
import ru.pin36bik.dto.UserLoginDTO;
import ru.pin36bik.dto.UserRegistrationDTO;
import ru.pin36bik.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.pin36bik.service.UserService;
import ru.pin36bik.service.KafkaProducerService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public UserController(UserService userService, KafkaProducerService kafkaProducerService) {
        this.userService = userService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        return new ResponseEntity<>(userService.registerUser(registrationDTO), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        UserDTO userDTO = userService.authorizeUser(userLoginDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/get_users")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/get_by_id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/get_by_email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/get-preset/{presetName}")
    public void getPresetByName(@PathVariable String presetName) {
        kafkaProducerService.sendPresetRequest(presetName);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateCurrentUser(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateCurrentUser(userDetails.getUsername(), userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAndArchiveCurrentUser(
            @PathVariable Long id) {
        User user = userService.getUserById(id);
        String email = user.getUsername();
        userService.deleteAndArchiveUser(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Возможно, понадобиться при тестах
//    @DeleteMapping("/{email}")
//    public HttpStatus deleteCurrentUser(@PathVariable("id") String email) {
//        userService.deleteUser(email);
//        return HttpStatus.OK;
//    }

}
