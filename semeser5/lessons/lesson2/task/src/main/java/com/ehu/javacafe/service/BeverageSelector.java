package com.ehu.javacafe.service;


import com.ehu.javacafe.entity.Beverage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BeverageSelector {
    private final int amountOfBeverages = new Random().nextInt(1, 4);
    private final CoffeeService beverageService;

    public BeverageSelector(CoffeeService beverageService) {
        this.beverageService = beverageService;
    }

    public List<Beverage> selectBeverage() {
        long beverageCount = beverageService.getBeverageCount();
        List<Beverage> order = new ArrayList<>();
        for (int i = 0; i < amountOfBeverages; i++) {

            Random random = new Random();
            long randomId = random.nextLong(1, beverageCount + 1);
            Beverage beverageById = beverageService.getBeverageById(randomId);
            order.add(beverageById);
        }
        return order;
    }
}
