package com.ehu.javacafe;

import com.ehu.javacafe.service.CoffeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainAnnotation {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        CoffeeService scenarioService = ctx.getBean(CoffeeService.class);

        scenarioService.getAllBeverages();

        ctx.close();
    }
}
