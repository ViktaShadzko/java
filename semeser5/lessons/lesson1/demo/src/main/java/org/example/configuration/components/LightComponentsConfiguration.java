package org.example.configuration.components;

import org.example.entity.Engine;
import org.example.entity.Gun;
import org.example.entity.ShieldGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("LightCruiser")
public class LightComponentsConfiguration {
    @Bean
    public Gun lazerGun1() {
        return new Gun("Pew", 10, 100, 2);
    }

    @Bean
    public Gun lazerGun2() {
        return new Gun("Pew", 10, 100, 2);
    }

    @Bean
    public ShieldGenerator shieldGenerator() {
        return new ShieldGenerator(150, 5);
    }

    @Bean
    public Engine engine() {
        return new Engine(300, 5);
    }
}
