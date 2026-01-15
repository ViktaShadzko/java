package org.example.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "SpaceShip entity representing a spaceship with weapons, shields, and engines")
public class SpaceShip {

    @Schema(description = "Unique identifier of the spaceship", example = "1")
    private int id;

    @Schema(description = "List of guns mounted on the spaceship")
    private List<Gun> guns;

    @Schema(description = "Shield generator protecting the spaceship")
    private ShieldGenerator shieldGenerator;

    @Schema(description = "Engine system powering the spaceship")
    private Engine engines;

    @Schema(description = "Name of the spaceship", example = "USS Enterprise")
    private String name;


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
