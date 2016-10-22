package com.idklabs.alexa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author nick.caballero
 */
@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        new SpringApplication(Application.class).run(args);
    }
}
