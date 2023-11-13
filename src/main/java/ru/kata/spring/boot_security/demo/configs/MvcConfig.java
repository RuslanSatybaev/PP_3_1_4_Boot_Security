package ru.kata.spring.boot_security.demo.configs;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry view) {
        view.addViewController("/user").setViewName("user");
        view.addViewController("/admin").setViewName("admin");
    }
}
