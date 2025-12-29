package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ShieldGenerator {
    private int capacity;
    private int rechargeRate;
}
