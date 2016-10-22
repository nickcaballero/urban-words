package com.idklabs.alexa.urbanwords.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author nick.caballero
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Definition {

    private String definition;
    private String example;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
