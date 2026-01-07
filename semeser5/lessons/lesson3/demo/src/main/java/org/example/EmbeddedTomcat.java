package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class EmbeddedTomcat {

    private static final int PORT = 8080;
    private static final String CONTEXT_PATH = "";

    public static void main(String[] args) throws LifecycleException {
        // Create Tomcat instance
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.getConnector(); // Explicitly create the default connector

        // Configure the web application context
        String webappDir = new File("src/main/webapp").getAbsolutePath();
        Context context = tomcat.addWebapp(CONTEXT_PATH, webappDir);

        // Set up resource directories
        File additionWebInfClasses = new File("target/classes");
        StandardRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);

        // Create Spring application context
        AnnotationConfigWebApplicationContext applicationContext =
                new AnnotationConfigWebApplicationContext();
        applicationContext.register(org.example.configuration.WebConfig.class);

        // Create and configure the DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

        // Add the DispatcherServlet to Tomcat
        Tomcat.addServlet(context, "dispatcher", dispatcherServlet);
        context.addServletMappingDecoded("/", "dispatcher");

        // Start the server
        tomcat.start();
        System.out.println("Embedded Tomcat started on port: " + PORT);
        System.out.println("Application available at: http://localhost:" + PORT);

        // Wait for the server to shutdown
        tomcat.getServer().await();
    }
}
