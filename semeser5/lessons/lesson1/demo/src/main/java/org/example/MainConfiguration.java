package org.example;

import org.example.configuration.components.HeavyComponentsConfiguration;
import org.example.configuration.components.LightComponentsConfiguration;
import org.example.configuration.SpaceShipConfiguration;
import org.example.entity.SpaceShip;
import org.example.service.ScenarioService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainConfiguration {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        String profile = args.length > 0 ? args[0] : System.getProperty("spring.profiles.active", "LightCruiser");
        ctx.getEnvironment().setActiveProfiles(profile);

        ctx.register(SpaceShipConfiguration.class,
                LightComponentsConfiguration.class,
                HeavyComponentsConfiguration.class,
                ScenarioService.class);

        ctx.refresh();

        SpaceShip ship = ctx.getBean(SpaceShip.class);

        ScenarioService scenarioService = ctx.getBean("scenarioService", ScenarioService.class);
        scenarioService.doScenario(ship);

        ctx.close();
    }
}
