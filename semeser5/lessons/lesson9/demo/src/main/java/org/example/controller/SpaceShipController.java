package org.example.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.entity.SpaceShip;
import org.example.service.SpaceShipService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/space/")
@Validated
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
    public SpaceShip getSpaceShip(@PathVariable @Positive(message = "ID must be positive") int id) {
        return service.getSpaceShip(id);
    }

    @PostMapping(path = "/spaceShip")
    public SpaceShip postSpaceShip(@RequestBody @Valid SpaceShip spaceShip) {
        return service.save(spaceShip);
    }

    @PatchMapping(path = "/spaceShip/{id}")
    public SpaceShip updateSpaceShip(@PathVariable @Positive(message = "ID must be positive") int id,
                                      @RequestBody SpaceShip spaceShip) {
        return service.patch(id, spaceShip);
    }

    @PutMapping(path = "/spaceShip/{id}")
    public SpaceShip putSpaceShip(@PathVariable @Positive(message = "ID must be positive") int id,
                                   @RequestBody @Valid SpaceShip spaceShip) {
        return service.put(id, spaceShip);
    }

    @DeleteMapping(path = "/spaceShip/{id}")
    public void deleteSpaceShip(@PathVariable @Positive(message = "ID must be positive") int id) {
        service.deleteSpaceShip(id);
    }

}
