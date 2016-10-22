package com.idklabs.alexa.urbanwords;

/**
 * @author nick.caballero
 */
public class WordDefinition {

    private final String word;
    private final String definition;

    public WordDefinition(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }
}
