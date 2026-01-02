package org.example.controller;

import org.example.entity.SpaceShip;
import org.example.service.SpaceShipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/space/")
public class SpaceShipController {
    private final SpaceShipService service;


    public SpaceShipController(SpaceShipService service) {
        this.service = service;
    }


    @GetMapping(path = "/spaceShip")
    public List<SpaceShip> getSpaceShip() {
        return service.getAllSpaceShips();
    }

    @GetMapping(path = "/spaceShip/{id}")
    public SpaceShip getSpaceShip(int id) {
        return service.getSpaceShip(id);
    }

    @PostMapping(path = "/spaceShip")
    public SpaceShip postSpaceShip(SpaceShip spaceShip) {
        SpaceShip spaceShipCreated = service.save(spaceShip);
        return spaceShipCreated;
    }

    @PatchMapping(path = "/spaceShip/{id}")
    public SpaceShip updateSpaceShip(int id, SpaceShip spaceShip) {
        return service.patch(id, spaceShip);
    }

    @PutMapping(path = "/spaceShip/{id}")
    public SpaceShip putSpaceShip(int id, SpaceShip spaceShip) {
        return service.put(id, spaceShip);
    }

    @DeleteMapping(path = "/spaceShip/{id}")
    public void deleteSpaceShip(int id) {
        service.deleteSpaceShip(id);
    }

}
