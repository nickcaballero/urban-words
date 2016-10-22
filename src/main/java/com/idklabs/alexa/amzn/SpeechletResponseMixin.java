package com.idklabs.alexa.amzn;


import com.amazon.speech.speechlet.SpeechletResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author nick.caballero
 */
public class SpeechletResponseMixin extends SpeechletResponse {

    @Override
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public boolean getShouldEndSession() {
        return super.getShouldEndSession();
    }
}
