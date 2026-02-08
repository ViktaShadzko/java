package ehu.java.cofffffeeeeee.repository.impl;

import ehu.java.cofffffeeeeee.entity.Beverage;
import ehu.java.cofffffeeeeee.repository.BeverageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that wraps Spring Data CrudRepository and implements BeverageRepository interface.
 * This demonstrates the Adapter pattern - converting the CrudRepository interface to our custom BeverageRepository interface.
 */
@Slf4j
@Repository
@Profile("jdbccrud")
@RequiredArgsConstructor
public class CrudRepositoryAdapter implements BeverageRepository {

    private final BeverageCrudRepository crudRepository;

    @Override
    public List<Beverage> getAllBeverages() {
        log.info("CrudRepository Adapter: Fetching all beverages from database");
        List<Beverage> beverages = new ArrayList<>();
        crudRepository.findAll().forEach(beverages::add);
        log.debug("CrudRepository Adapter: Total beverages fetched: {}", beverages.size());
        return beverages;
    }

    @Override
    public boolean addBeverage(Beverage beverage) {
        log.info("CrudRepository Adapter: Adding beverage: {}", beverage.getName());
        try {
            Beverage savedBeverage = crudRepository.save(beverage);
            beverage.setId(savedBeverage.getId());
            log.info("CrudRepository Adapter: Beverage added successfully with ID: {}", beverage.getId());
            return true;
        } catch (Exception e) {
            log.error("CrudRepository Adapter: Error adding beverage", e);
            return false;
        }
    }

    @Override
    public Beverage deleteBeverage(long id) {
        log.info("CrudRepository Adapter: Deleting beverage with ID: {}", id);
        Beverage beverage = getBeverageById(id);

        if (beverage != null) {
            crudRepository.deleteById(id);
            log.info("CrudRepository Adapter: Beverage with ID {} deleted successfully", id);
            return beverage;
        }

        log.warn("CrudRepository Adapter: Beverage with ID {} not found, cannot delete", id);
        return null;
    }

    @Override
    public Beverage getBeverageById(long id) {
        log.debug("CrudRepository Adapter: Fetching beverage by ID: {}", id);
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public long getBeverageCount() {
        log.debug("CrudRepository Adapter: Fetching beverage count");
        long count = crudRepository.count();
        log.debug("CrudRepository Adapter: Total beverage count: {}", count);
        return count;
    }
}

