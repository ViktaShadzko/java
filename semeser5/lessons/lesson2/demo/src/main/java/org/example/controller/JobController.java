package org.example.controller;

import org.example.service.ScenarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/job")
public class JobController {
    private final ScenarioService service;

    public JobController(ScenarioService service) {
        this.service = service;
    }

    @ResponseBody
    public String startJob(@RequestParam(name = "firstShipId") int firstShipId,
                        @RequestHeader(name = "secondShipId") int secondShipId) {
        return service.startBattle(firstShipId, secondShipId);
    }
}

