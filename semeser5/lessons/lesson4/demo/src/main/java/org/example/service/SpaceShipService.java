package org.example.service;

import org.example.entity.SpaceShip;
import org.example.exception.SpaceShipNotFoundException;
import org.example.repository.SpaceShipInMemoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceShipService {
    private final SpaceShipInMemoryRepository repository;

    public SpaceShipService(SpaceShipInMemoryRepository repository) {
        this.repository = repository;
    }

    public SpaceShip getSpaceShip(int spaceShipId) {
        Optional<SpaceShip> byId = repository.findById(spaceShipId);
        return byId.orElseThrow(()->new SpaceShipNotFoundException("SpaceShip with id " + spaceShipId + " not found"));
    }

    public List<SpaceShip> getAllSpaceShips() {
        List<SpaceShip> all = repository.findAll();
        return all;
    }

    public SpaceShip save(SpaceShip spaceShip) {
        return repository.save(spaceShip);
    }

    public void deleteSpaceShip(int id) {
        SpaceShip spaceShip = getSpaceShip(id);
        repository.delete(spaceShip);
    }

    public SpaceShip patch(int id, SpaceShip spaceShip) {
        SpaceShip existingSpaceShip = getSpaceShip(id);

        if(spaceShip.getEngines() != null) {
            existingSpaceShip.setEngines(spaceShip.getEngines());
        }
        if(spaceShip.getShieldGenerator() != null) {
            existingSpaceShip.setShieldGenerator(spaceShip.getShieldGenerator());
        }
        if(spaceShip.getGuns() != null) {
            existingSpaceShip.setGuns(spaceShip.getGuns());
        }
        if(spaceShip.getId() != 0) {
            existingSpaceShip.setId(spaceShip.getId());
        }
        return repository.save(existingSpaceShip);
    }

    public SpaceShip put(int id, SpaceShip spaceShip) {
        spaceShip.setId(id);
        return repository.save(spaceShip);
    }
}
