package ehu.java.cofffffeeeeee.service;

import ehu.java.cofffffeeeeee.entity.Beverage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class BeverageSelector {
    private final CoffeeService coffeeService;

    public BeverageSelector(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    public List<Beverage> selectBeverage() {
        Random random = new Random();
        int amountOfBeverages = random.nextInt(1, 4);
        long beverageCount = coffeeService.getBeverageCount();
        List<Beverage> order = new ArrayList<>();

        log.info("Selecting {} random beverages", amountOfBeverages);

        for (int i = 0; i < amountOfBeverages; i++) {
            long randomId = random.nextLong(1, beverageCount + 1);
            Beverage beverageById = coffeeService.getBeverageById(randomId);
            if (beverageById != null) {
                order.add(beverageById);
            }
        }

        log.info("Selected {} beverages", order.size());
        return order;
    }
}

