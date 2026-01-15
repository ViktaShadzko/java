package com.ehu.javacafe.service;


import com.ehu.javacafe.entity.Beverage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BeverageSelectorRandom implements BeverageSelector {
    private final int amountOfBeverages = ThreadLocalRandom.current().nextInt(1, 5);
    private final CoffeeService beverageService;

    @Autowired
    public BeverageSelectorRandom(CoffeeService beverageService) {
        this.beverageService = beverageService;
    }

    @Override
    public List<Beverage> selectBeverage() {
        long beverageCount = beverageService.getBeverageCount();
        List<Beverage> order = new ArrayList<>();
        for (int i = 0; i < amountOfBeverages; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(1, beverageCount + 1);
            Beverage beverageById = beverageService.getBeverageById(randomId);
            order.add(beverageById);
        }
        return order;
    }
}
