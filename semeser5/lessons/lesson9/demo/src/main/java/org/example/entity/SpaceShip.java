package org.example.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class SpaceShip {
    @Positive(message = "ID must be a positive number")
    private int id;

    @NotEmpty(message = "Spaceship must have at least one gun")
    @Valid
    private List<Gun> guns;

    @NotNull(message = "Shield generator is required")
    @Valid
    private ShieldGenerator shieldGenerator;

    @NotNull(message = "Engine is required")
    @Valid
    private Engine engines;

    @NotBlank(message = "Spaceship name cannot be blank")
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
