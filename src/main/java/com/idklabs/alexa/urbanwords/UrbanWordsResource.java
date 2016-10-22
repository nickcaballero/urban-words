package com.idklabs.alexa.urbanwords;

import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.idklabs.alexa.urbanwords.api.Definition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

/**
 * @author nick.caballero
 */
@RestController
@RequestMapping("urban-words")
public class UrbanWordsResource {

    @Autowired
    private UrbanWordsService service;

    @PostMapping("/day")
    public SpeechletResponseEnvelope getWordOfTheDay(@RequestBody SpeechletResponseEnvelope request)
                    throws IOException {

        // Get word of the day and define it
        String wordOfTheDay = service.getWordOfTheDay();
        Optional<Definition> definition = service.getDefinition(wordOfTheDay)
                                                 .getDefinitions()
                                                 .stream()
                                                 .findFirst();

        // Build output speech if nay
        SpeechletResponse response = new SpeechletResponse();
        if (definition.isPresent()) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText(definition.get()
                                           .getDefinition());
            response.setOutputSpeech(outputSpeech);
        }

        // Output the envelope
        SpeechletResponseEnvelope envelope = new SpeechletResponseEnvelope();
        envelope.setResponse(response);
        return envelope;
    }
}
