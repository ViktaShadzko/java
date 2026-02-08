package ehu.java.cofffffeeeeee.repository.impl;

import ehu.java.cofffffeeeeee.entity.Beverage;
import ehu.java.cofffffeeeeee.repository.BeverageRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class JSONBeverageRepository implements BeverageRepository {
    private static final String BEVERAGES_FILE = "beverages.json";
    private final List<Beverage> beverages;

    public JSONBeverageRepository() {
        this.beverages = loadBeveragesFromJson();
    }

    private List<Beverage> loadBeveragesFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(BEVERAGES_FILE)) {
            if (inputStream == null) {
                log.error("Could not find {} in resources", BEVERAGES_FILE);
                throw new RuntimeException("Could not find " + BEVERAGES_FILE + " in resources");
            }
            List<Beverage> loadedBeverages = new ArrayList<>(objectMapper.readValue(inputStream, new TypeReference<List<Beverage>>() {}));
            log.info("Successfully loaded {} beverages from JSON file", loadedBeverages.size());
            return loadedBeverages;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load beverages from JSON file", e);
        }
    }

    @Override
    public List<Beverage> getAllBeverages() {
        log.info("Fetching all beverages from JSON repository");
        log.debug("Total beverages available: {}", beverages.size());
        return beverages;
    }

    @Override
    public boolean addBeverage(Beverage beverage) {
        return beverages.add(beverage);
    }

    @Override
    public Beverage deleteBeverage(long id) {
        Beverage beverageToDelete = getBeverageById(id);
        if (beverageToDelete != null) {
            beverages.remove(beverageToDelete);
        } else {
            log.warn("Beverage with ID {} not found, cannot delete", id);
        }
        return beverageToDelete;
    }

    @Override
    public Beverage getBeverageById(long id) {
        log.debug("Fetching beverage by ID: {}", id);
        return beverages.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public long getBeverageCount() {
        return beverages.size();
    }
}

