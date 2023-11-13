package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.exceptions.UserCreateException;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/allRoles")
    public List<Role> getAllRoles() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUser(id);
    }

    @GetMapping("/rgs")
    public User showRegisUsers() {
        return userService.registration();
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid User user,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errMsg;
            List<FieldError> errors = bindingResult.getFieldErrors();
            errMsg = errors.stream()
                    .map(error -> error.getField() + " - " + error.getDefaultMessage() + ";")
                    .collect(Collectors.joining());
            throw new UserCreateException(errMsg);
        }
        userService.saveUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        userService.getUser(id);
        userService.deleteUser(id);
        return ResponseEntity.ok("User with id: " + id + " delete successfully!");
    }

    @PatchMapping("updateUser/{id}")
    public ResponseEntity<String> updateUser(@RequestBody @Valid User user,
                                             @PathVariable("id") long id) {
        userService.updateUser(id, user);
        return ResponseEntity.ok("User with id: " + id + " update successfully!");
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(Exception ex) {
        LocalDateTime localDateTime = LocalDateTime.now();
        UserErrorResponse response = new UserErrorResponse(
                ex.getMessage(), localDateTime
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
