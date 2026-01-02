package org.example.entity;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ShieldGenerator {
    @Positive(message = "Capacity must be positive")
    private int capacity;

    @Positive(message = "Recharge rate must be positive")
    private int rechargeRate;
}
