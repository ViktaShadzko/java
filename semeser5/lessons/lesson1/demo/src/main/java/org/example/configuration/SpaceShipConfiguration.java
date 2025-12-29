package org.example.configuration;

import org.example.entity.Engine;
import org.example.entity.Gun;
import org.example.entity.ShieldGenerator;
import org.example.entity.SpaceShip;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;



@Configuration
public class SpaceShipConfiguration {

    @Bean
    public SpaceShip lightCruiser(List<Gun> guns, Engine engine, ShieldGenerator shieldGenerator) {
        return new SpaceShip(guns, shieldGenerator, engine);
    }




}
