package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class Init {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public Init(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostConstruct
    public void initializeDataBase() {
        roleService.save(new Role("ROLE_ADMIN"));
        roleService.save(new Role("ROLE_USER"));
        roleService.save(new Role("ROLE_MANAGER"));

        Set<Role> adminRole = new HashSet<>();
        Set<Role> userRole = new HashSet<>();
        Set<Role> allRoles = new HashSet<>();

        adminRole.add(roleService.findByRoleName("ROLE_ADMIN"));
        userRole.add(roleService.findByRoleName("ROLE_USER"));
        allRoles.add(roleService.findByRoleName("ROLE_USER"));
        allRoles.add(roleService.findByRoleName("ROLE_MANAGER"));

        userService.saveUser(new User("admin", 32, "admin", adminRole));
        userService.saveUser(new User("user", 28, "user", userRole));
        userService.saveUser(new User("Mike", 50, "testPass", allRoles));
    }
}
