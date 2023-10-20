package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleService {
    Role findByRoleName(String name);

    void save(Role role);

    List<Role> getAll();
}
