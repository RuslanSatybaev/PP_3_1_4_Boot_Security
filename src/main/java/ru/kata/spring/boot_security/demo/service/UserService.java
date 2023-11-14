package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    User authorization();

    void saveUser(User user);

    User getUser(Long id);

    List<User> getAllUsers();

    void updateUser(Long id, User user);

    void deleteUser(Long id);
}
