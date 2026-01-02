package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Gun {
    private final String noise;
    private final int damage;
    private final int range;
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
