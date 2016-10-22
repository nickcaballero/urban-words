package com.idklabs.alexa.urbanwords;

import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.idklabs.alexa.amzn.AbstractSpeechlet;
import com.idklabs.alexa.urbanwords.api.Definition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

/**
 * @author nick.caballero
 */
@RestController
@RequestMapping("urban-words")
public class UrbanWordsResource extends AbstractSpeechlet {

    private static final String INVALID_CHARACTERS = "[\\[\\]!\\\\\\r\\n]";

    @Autowired
    private UrbanWordsService service;

    @Intent("WordOfTheDay")
    public SpeechletResponse getWordOfTheDay() throws IOException {
        String word = service.getWordOfTheDay();
        return buildDefinitionResponse("The word of the day is " + word + ", ", word);
    }

    @Intent("DefineWord")
    public SpeechletResponse getDefinition(IntentRequest request) throws IOException {
        String word = request.getIntent()
                             .getSlot("Word")
                             .getValue();
        return buildDefinitionResponse(word + " means ", word);
    }


    private SpeechletResponse buildDefinitionResponse(String prefix, String word) {
        Optional<Definition> definition = service.getDefinition(word)
                                                 .getDefinitions()
                                                 .stream()
                                                 .findFirst();

        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        response.setOutputSpeech(outputSpeech);

        if (definition.isPresent()) {
            outputSpeech.setText((prefix + definition.get()
                                                     .getDefinition()).replaceAll(
                                                                     INVALID_CHARACTERS, ""));
        } else {
            outputSpeech.setText("No definition found for " + word);
        }

        return response;
    }
}
