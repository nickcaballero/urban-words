package com.idklabs.alexa;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.idklabs.alexa.amzn.SpeechletResponseMixin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author nick.caballero
 */
@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        new SpringApplication(Application.class).run(args);
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.mixIn(SpeechletResponse.class, SpeechletResponseMixin.class);
        return builder;
    }
}
