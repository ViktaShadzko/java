package ehu.java.cofffffeeeeee.controller;

import ehu.java.cofffffeeeeee.entity.Beverage;
import ehu.java.cofffffeeeeee.service.BeverageSelector;
import ehu.java.cofffffeeeeee.service.CoffeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/beverages")
@Validated
public class BeverageController {

    private final CoffeeService coffeeService;
    private final BeverageSelector beverageSelector;

    public BeverageController(CoffeeService coffeeService, BeverageSelector beverageSelector) {
        this.coffeeService = coffeeService;
        this.beverageSelector = beverageSelector;
    }

    @GetMapping
    public ResponseEntity<List<Beverage>> getBeverages() {
        log.info("REST: GET all beverages request received");
        List<Beverage> beverages = coffeeService.getAllBeverages();
        return ResponseEntity.ok(beverages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beverage> getBeverageById(@PathVariable long id) {
        log.info("REST: GET beverage by ID: {}", id);
        Beverage beverage = coffeeService.getBeverageById(id);
        if (beverage != null) {
            return ResponseEntity.ok(beverage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/string")
    public ResponseEntity<String> getBeveragesAsString() {
        log.info("REST: GET all beverages as string");
        List<Beverage> beverages = coffeeService.getAllBeverages();
        String result = beverages.stream()
                .map(b -> String.format("%s - $%.2f", b.getName(), b.getPrice()))
                .collect(Collectors.joining(", "));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/random")
    public ResponseEntity<List<Beverage>> getRandomBeverages() {
        log.info("REST: GET random beverages");
        List<Beverage> selectedBeverages = beverageSelector.selectBeverage();
        return ResponseEntity.ok(selectedBeverages);
    }

    @PostMapping
    public ResponseEntity<Beverage> addBeverage(@Valid @RequestBody Beverage beverage) {
        log.info("REST: POST new beverage: {}", beverage.getName());
        boolean added = coffeeService.addBeverage(beverage);
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).body(beverage);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Beverage> deleteBeverage(@PathVariable long id) {
        log.info("REST: DELETE beverage with ID: {}", id);
        Beverage deletedBeverage = coffeeService.deleteBeverage(id);
        if (deletedBeverage != null) {
            return ResponseEntity.ok(deletedBeverage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getBeverageCount() {
        log.info("REST: GET beverage count");
        long count = coffeeService.getBeverageCount();
        return ResponseEntity.ok(count);
    }
}

