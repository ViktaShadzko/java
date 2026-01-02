package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class SpaceShip {
    private int id;
    private List<Gun> guns;
    private ShieldGenerator shieldGenerator;
    private Engine engines;


    public String fire() {
        StringBuilder result = new StringBuilder();
        for (Gun gun : guns) {
            if (gun.fire()) {
                System.out.println("Firing gun with damage: " + gun.getDamage() + " and noise: " + gun.getNoise());
                result.append("Fired gun with damage: " + gun.getDamage() + " and noise: " + gun.getNoise());
            } else {
                System.out.println("Gun failed to fire.");
                result.append("Gun failed to fire.");
            }
        }
        return result.toString();
    }

    public void shield() {
        System.out.println("Shield capacity: " + shieldGenerator.getCapacity() + ", recharge rate: " + shieldGenerator.getRechargeRate());
    }

    public void engines() {
        System.out.println("Engine power: " + engines.getPower() + ", fuel consumption: " + engines.getFuelConsumption());
    }
}
