package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/welcome")
@Controller
public class WelcomeController {

    @GetMapping
    public String welcome(
            Model model,
            @RequestParam(defaultValue = "Guest", name = "user") String name) {
        model.addAttribute("name", name);
        return "index";
    }
}
