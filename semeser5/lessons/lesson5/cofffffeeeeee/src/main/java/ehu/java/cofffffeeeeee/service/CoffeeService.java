package ehu.java.cofffffeeeeee.service;

import ehu.java.cofffffeeeeee.entity.Beverage;
import ehu.java.cofffffeeeeee.repository.BeverageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CoffeeService {
    private final BeverageRepository beverageRepository;

    public CoffeeService(BeverageRepository beverageRepository) {
        this.beverageRepository = beverageRepository;
    }

    public Beverage getBeverageById(long id) {
        log.info("Service: Fetching beverage by ID: {}", id);
        Beverage beverage = beverageRepository.getBeverageById(id);
        if (beverage != null) {
            log.debug("Service: Found beverage: {}", beverage.getName());
        } else {
            log.warn("Service: Beverage with ID {} not found", id);
        }
        return beverage;
    }

    public List<Beverage> getAllBeverages() {
        log.info("Service: Fetching all beverages");
        List<Beverage> beverages = beverageRepository.getAllBeverages();
        log.debug("Service: Retrieved {} beverages", beverages.size());
        return beverages;
    }

    public long getBeverageCount() {
        return beverageRepository.getBeverageCount();
    }

    public boolean addBeverage(Beverage beverage) {
        log.info("Service: Adding new beverage: {}", beverage.getName());
        return beverageRepository.addBeverage(beverage);
    }

    public Beverage deleteBeverage(long id) {
        log.info("Service: Deleting beverage with ID: {}", id);
        return beverageRepository.deleteBeverage(id);
    }
}

