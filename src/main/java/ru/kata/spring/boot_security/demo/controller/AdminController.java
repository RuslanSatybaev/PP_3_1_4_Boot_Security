package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    @Lazy
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/new")
    public String addNewUser(Model model) {
        model.addAttribute("newUser", new User());
        return "add-new-user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "edit";
    }

    @PatchMapping("/updateUser")
    public String updateUser(@ModelAttribute("newUser") User user, @RequestParam("id") long id) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
