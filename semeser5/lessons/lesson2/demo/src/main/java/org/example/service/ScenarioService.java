package org.example.service;

import org.example.entity.SpaceShip;
import org.springframework.stereotype.Service;

@Service
public class ScenarioService {
    private final SpaceShipService spaceShipService;

    public ScenarioService(SpaceShipService spaceShip) {
        this.spaceShipService = spaceShip;
    }

    public void doScenario(int spaceShipId) throws InterruptedException {
        SpaceShip spaceShip = spaceShipService.getSpaceShip(spaceShipId);
        spaceShip.fire();
        Thread.sleep(1000);
        spaceShip.fire();

        Thread.sleep(1000);
        spaceShip.fire();

        spaceShip.engines();
        spaceShip.shield();

    }

    public String startBattle(int firstShipId, int secondShipId) {
        SpaceShip spaceShip = spaceShipService.getSpaceShip(firstShipId);
        SpaceShip spaceShip2 = spaceShipService.getSpaceShip(secondShipId);

        String fire = spaceShip.fire();
        String fire2 = spaceShip2.fire();
        return fire + "\n" + fire2;
    }
}
