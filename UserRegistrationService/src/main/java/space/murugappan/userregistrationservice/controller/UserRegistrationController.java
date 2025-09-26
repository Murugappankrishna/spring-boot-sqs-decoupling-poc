package space.murugappan.userregistrationservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.murugappan.userregistrationservice.dao.User;
import space.murugappan.userregistrationservice.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserRegistrationController {
    final UserService userService;
    UserRegistrationController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/registeruser")
    ResponseEntity<User> registerUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
    }
}
