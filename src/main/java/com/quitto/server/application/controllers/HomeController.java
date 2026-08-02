package com.quitto.server.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Novo frontend: entry point faz o redirect para login/dashboard.
        return "redirect:/app/index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/app/pages/login.html";
    }
}
