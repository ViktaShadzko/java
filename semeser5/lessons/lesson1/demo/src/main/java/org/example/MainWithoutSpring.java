package org.example;

import org.example.entity.Engine;
import org.example.entity.Gun;
import org.example.entity.ShieldGenerator;
import org.example.entity.SpaceShip;
import org.example.service.ScenarioService;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainWithoutSpring {
    public static void main(String[] args) throws InterruptedException {

        ScenarioService scenarioService = new ScenarioService();


        Gun lazerGun = new Gun("Pew", 10, 100, 2);
        Gun lazerGun2 = new Gun("Pew", 10, 100, 2);
        Gun rocketLauncher = new Gun("Boom", 50, 300, 5);
        Gun kineticGun = new Gun("Bang", 20, 150, 3);

        ShieldGenerator shieldGenerator = new ShieldGenerator(200, 5);
        Engine engines = new Engine(300, 5);

        SpaceShip spaceShip = new SpaceShip(List.of(lazerGun,lazerGun2), shieldGenerator, engines);
        scenarioService.doScenario(spaceShip);
    }
}