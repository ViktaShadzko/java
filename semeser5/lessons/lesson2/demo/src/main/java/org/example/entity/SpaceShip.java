package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Getter
@Component("spaceShip")
public class SpaceShip {
    private int id;
    private List<Gun> guns;
    private ShieldGenerator shieldGenerator;
    private Engine engines;


    public void fire() {
        for (Gun gun : guns) {
            if (gun.fire()) {
                System.out.println("Firing gun with damage: " + gun.getDamage() + " and noise: " + gun.getNoise());
            } else {
                System.out.println("Gun failed to fire.");
            }
        }
    }

    public void shield() {
        System.out.println("Shield capacity: " + shieldGenerator.getCapacity() + ", recharge rate: " + shieldGenerator.getRechargeRate());
    }

    public void engines() {
        System.out.println("Engine power: " + engines.getPower() + ", fuel consumption: " + engines.getFuelConsumption());
    }
}
