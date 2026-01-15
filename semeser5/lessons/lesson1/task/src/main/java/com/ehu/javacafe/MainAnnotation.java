package com.ehu.javacafe;

import com.ehu.javacafe.entity.Beverage;
import com.ehu.javacafe.service.BeverageSelectorRandom;
import com.ehu.javacafe.service.CoffeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@ComponentScan(basePackages = "com.ehu.javacafe")
public class MainAnnotation {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainAnnotation.class);
        CoffeeService scenarioService = ctx.getBean(CoffeeService.class);

        scenarioService.getAllBeverages();

        List<Beverage> beverages = ctx.getBean(BeverageSelectorRandom.class).selectBeverage();
        ctx.close();
    }
}
