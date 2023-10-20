package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    @Override
    public User getUser(long id) {
        return repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("id not found - " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll().stream().distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateUser(long id, User user) {
        User updateUser = getUser(id);
        updateUser.setUsername(user.getUsername());
        updateUser.setAge(user.getAge());
        updateUser.setRoleList(user.getRoleList());
        repository.save(updateUser);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(repository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found - " + username));
    }
}
