package org.example.service;

import org.example.entity.SpaceShip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScenarioService {

    public void doScenario(SpaceShip spaceShip) throws InterruptedException {
        spaceShip.fire();
        Thread.sleep(1000);
        spaceShip.fire();

        Thread.sleep(1000);
        spaceShip.fire();

        spaceShip.engines();
        spaceShip.shield();

    }
}
