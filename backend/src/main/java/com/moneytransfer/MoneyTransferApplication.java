package com.moneytransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Money Transfer System
 *
 * @SpringBootApplication is a convenience annotation that combines:
 * - @Configuration: Marks this as a configuration class
 * - @EnableAutoConfiguration: Tells Spring Boot to auto-configure based on dependencies
 * - @ComponentScan: Scans for Spring components in this package and sub-packages
 */
@SpringBootApplication
public class MoneyTransferApplication {

    /**
     * Main method - starts the Spring Boot application
     */
    public static void main(String[] args) {
        SpringApplication.run(MoneyTransferApplication.class, args);

        System.out.println("\n===========================================");
        System.out.println("💰 Money Transfer System Started!");
        System.out.println("🌐 API: http://localhost:8080");
        System.out.println("🗄️  H2 Console: http://localhost:8080/h2-console");
        System.out.println("===========================================\n");
    }
}