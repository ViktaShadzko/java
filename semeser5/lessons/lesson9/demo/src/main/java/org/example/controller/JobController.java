package org.example.controller;

import org.example.service.ScenarioService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
    private final ScenarioService service;

    public JobController(ScenarioService service) {
        this.service = service;
    }


}

