package org.example.repository;

import org.example.entity.SpaceShip;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SpaceShipInMemoryRepository {
    private final Map<Integer, SpaceShip> spaceShips = new HashMap<>();
    private int idCounter = 1;

    public SpaceShip save(SpaceShip spaceShip) {
        // If the spaceship doesn't have an ID (or has ID 0), assign a new one
        if (spaceShip.getId() == 0) {
            SpaceShip newSpaceShip = new SpaceShip(
                idCounter++,
                spaceShip.getGuns(),
                spaceShip.getShieldGenerator(),
                spaceShip.getEngines()
            );
            spaceShips.put(newSpaceShip.getId(), newSpaceShip);
            return newSpaceShip;
        } else {
            spaceShips.put(spaceShip.getId(), spaceShip);
            return spaceShip;
        }
    }

    public List<SpaceShip> findAll() {
        return new ArrayList<>(spaceShips.values());
    }

    public Optional<SpaceShip> findById(int id) {
        return Optional.ofNullable(spaceShips.get(id));
    }

    public boolean existsById(int id) {
        return spaceShips.containsKey(id);
    }

    public void delete(SpaceShip spaceShip) {
        spaceShips.remove(spaceShip.getId());
    }

    public void deleteById(int id) {
        spaceShips.remove(id);
    }

    public int count() {
        return spaceShips.size();
    }

    public void clear() {
        spaceShips.clear();
    }
}
