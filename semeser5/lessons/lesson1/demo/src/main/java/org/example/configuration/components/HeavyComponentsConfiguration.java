package org.example.configuration.components;

import org.example.entity.Engine;
import org.example.entity.Gun;
import org.example.entity.ShieldGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("HeavyCruiser")
public class HeavyComponentsConfiguration {

    @Bean
    public Gun rocketLauncher() {
        return new Gun("Boom", 50, 300, 5);
    }

    @Bean
    public Gun kineticGun() {
        return new Gun("Bang", 20, 150, 3);
    }

    @Bean
    public ShieldGenerator heavyShieldGenerator() {
        return new ShieldGenerator(400, 10);
    }

    @Bean
    public Engine heavyEngine() {
        return new Engine(600, 12);
    }
}
