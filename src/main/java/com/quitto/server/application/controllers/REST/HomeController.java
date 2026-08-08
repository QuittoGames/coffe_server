package com.quitto.server.application.controllers.REST;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("coffee")
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
