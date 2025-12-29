package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class MainEmbeddedTomcat {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector(); // Creates default connector

        // Create a context
        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();
        Context context = tomcat.addContext(contextPath, docBase);

        // Configure Spring MVC
        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
        springContext.register(org.example.configuration.WebConfig.class);

        // Add DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(springContext);
        String servletName = "dispatcher";
        Tomcat.addServlet(context, servletName, dispatcherServlet);
        context.addServletMappingDecoded("/*", servletName);

        tomcat.start();
        System.out.println("Tomcat started on port 8080");
        System.out.println("Access the application at: http://localhost:8080/spaceShip/1");
        tomcat.getServer().await();
    }
}

