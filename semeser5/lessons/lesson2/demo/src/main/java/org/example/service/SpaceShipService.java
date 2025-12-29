package org.example.service;

import org.example.entity.SpaceShip;
import org.example.repository.SpaceShipInMemoryRepository;
import org.springframework.stereotype.Service;

@Service
public class SpaceShipService {
    private final SpaceShipInMemoryRepository repository;

    public SpaceShipService(SpaceShipInMemoryRepository repository) {
        this.repository = repository;
    }

    public SpaceShip getSpaceShip(int spaceShipId) {
        return repository.findById(spaceShipId).orElse(null);
    }

    public SpaceShip postSpaceShip(SpaceShip spaceShip) {
        return repository.save(spaceShip);
    }
}
