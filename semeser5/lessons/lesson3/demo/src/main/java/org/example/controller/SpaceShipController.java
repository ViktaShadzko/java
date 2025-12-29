package org.example.controller;

import org.example.entity.SpaceShip;
import org.example.service.SpaceShipService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/space/")
public class SpaceShipController {
    private final SpaceShipService service;


    public SpaceShipController(SpaceShipService service) {
        this.service = service;
    }

    @GetMapping(path = "/spaceShip/{id}")
    public SpaceShip getSpaceShip(int id) {
        return service.getSpaceShip(id);
    }

    @PostMapping(path = "/spaceShip")
    public SpaceShip postSpaceShip(SpaceShip spaceShip) {
        SpaceShip spaceShipCreated = service.postSpaceShip(spaceShip);
        return spaceShipCreated;
    }

}
