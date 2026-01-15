package org.example;

import org.example.entity.SpaceShip;
import org.example.service.ScenarioService;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainXML {
    public static void main(String[] args) throws InterruptedException {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();


        String profile = args.length > 0 ? args[0] : System.getProperty("spring.profiles.active", "LightCruiser");
        ctx.getEnvironment().setActiveProfiles(profile);

        ctx.load("classpath:bean.xml");
        ctx.refresh();

        ScenarioService scenarioService = ctx.getBean(ScenarioService.class);
        SpaceShip ship = ctx.getBean(SpaceShip.class);
        ctx.getBean("loger");
        ctx.getBean("loger");
        ctx.getBean("loger");
        ctx.getBean("loger");
        ctx.getBean("loger");
        scenarioService.doScenario(ship);

        ctx.close();
    }
}
