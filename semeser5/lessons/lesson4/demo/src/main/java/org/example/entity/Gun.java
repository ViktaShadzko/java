package org.example.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Gun {
    @NotBlank(message = "Gun noise cannot be blank")
    private final String noise;

    @Positive(message = "Damage must be positive")
    private final int damage;

    @Positive(message = "Range must be positive")
    private final int range;

    @PositiveOrZero(message = "Reload time must be zero or positive")
    private final int reloadTime;

    private Instant lastFireTime;

    public boolean fire() {

        if (lastFireTime != null && Instant.now().isBefore(lastFireTime.plusSeconds(reloadTime))) {
            return false; // Gun is still reloading
        }
        lastFireTime = Instant.now();
        return true;
    }
}
