package com.idklabs.alexa.urbanwords.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author nick.caballero
 */
public class DefinitionResponse {

    @JsonProperty("list")
    private List<Definition> definitions;
    private List<String> sounds;

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public List<String> getSounds() {
        return sounds;
    }

    public void setSounds(List<String> sounds) {
        this.sounds = sounds;
    }
}
