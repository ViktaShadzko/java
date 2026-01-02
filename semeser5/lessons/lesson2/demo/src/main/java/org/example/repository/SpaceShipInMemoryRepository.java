package org.example.repository;

import org.example.entity.Engine;
import org.example.entity.Gun;
import org.example.entity.ShieldGenerator;
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

    public SpaceShipInMemoryRepository() {
        SpaceShip spaceShip1 =  SpaceShip.builder()
                .id(1)
                .engines(new Engine(200, 9))
                .shieldGenerator(new ShieldGenerator(1000, 500))
                .guns(List.of(new Gun("Pauuuuuuuuuuu",30,10,10), new Gun("Pew", 5,2,1)))
                .build();

        SpaceShip spaceShip2 =  SpaceShip.builder()
                .id(2)
                .engines(new Engine(150, 7))
                .shieldGenerator(new ShieldGenerator(800, 300))
                .guns(List.of(new Gun("Blam",20,5,5)))
                .build();
        spaceShips.put(spaceShip1.getId(), spaceShip1);

        spaceShips.put(spaceShip2.getId(), spaceShip2);
    }

    public SpaceShip save(SpaceShip spaceShip) {
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
