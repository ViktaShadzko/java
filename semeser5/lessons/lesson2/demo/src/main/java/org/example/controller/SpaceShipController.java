package org.example.controller;

import org.example.entity.SpaceShip;
import org.example.service.SpaceShipService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/space")
@RestController
public class SpaceShipController {
    private final SpaceShipService service;


    public SpaceShipController(SpaceShipService service) {
        this.service = service;
    }

    @RequestMapping(path = "/spaceShip/{id}", method = RequestMethod.GET)
    public SpaceShip getSpaceShip(@PathVariable("id") int id) {
        return service.getSpaceShip(id);
    }

    @RequestMapping(path = "/spaceShip", method = RequestMethod.POST)
    public SpaceShip postSpaceShip(@RequestBody SpaceShip spaceShip) {
        return service.postSpaceShip(spaceShip);
    }

}
