package org.example.entity;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Engine {
    @Positive(message = "Power must be positive")
    private int power;

    @Positive(message = "Fuel consumption must be positive")
    private int fuelConsumption;
}
