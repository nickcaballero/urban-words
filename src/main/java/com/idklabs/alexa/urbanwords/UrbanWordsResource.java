package com.idklabs.alexa.urbanwords;

import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.idklabs.alexa.amzn.AbstractSpeechlet;
import com.idklabs.alexa.urbanwords.api.Definition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author nick.caballero
 */
@RestController
@RequestMapping("urban-words")
public class UrbanWordsResource extends AbstractSpeechlet {

    private static final String INVALID_CHARS = "[\\[\\]!\\\\\\r\\n:|]";
    private static final String LAST_WORD = "lastWord";
    private static final String INDEX_PREFIX = "$";

    @Autowired
    private UrbanWordsService service;

    @Intent("WordOfTheDay")
    public SpeechletResponse getWordOfTheDay(Session session) throws IOException {
        String word = service.getWordOfTheDay();
        return buildDefinitionResponse("The word of the day is " + word + ", ", word, session);
    }

    @Intent("DefineWord")
    public SpeechletResponse getDefinition(IntentRequest request, Session session)
                    throws IOException {
        String word = request.getIntent()
                             .getSlot("Word")
                             .getValue();
        return buildDefinitionResponse(word + " is ", word, session);
    }

    @Intent("AlternateDefinition")
    public SpeechletResponse getNextDefinition(Session session) {
        String word = session.getAttribute(LAST_WORD)
                             .toString();
        return buildDefinitionResponse("Another definition for " + word + " is ", word, session);
    }


    @Intent("StopDefinition")
    public SpeechletResponse stop(Session session) {
        SpeechletResponse response = new SpeechletResponse();
        response.setShouldEndSession(true);
        return response;
    }

    private SpeechletResponse buildDefinitionResponse(String prefix, String word, Session session) {
        String wordKey = INDEX_PREFIX + word.hashCode();
        int definitionIndex = getDefinitionIndex(session, wordKey);

        List<Definition> definitions = service.getDefinition(word)
                                              .getDefinitions();
        Definition definition = definitions.isEmpty() ? null : definitions.get(definitionIndex);
        session.setAttribute(wordKey, definitionIndex);
        session.setAttribute(LAST_WORD, word);

        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        response.setOutputSpeech(outputSpeech);

        if (definition != null) {
            String speech = (prefix + definition.getDefinition()).replaceAll(INVALID_CHARS, "");
            if (definitions.size() > definitionIndex + 1) {
                speech += ". Would you like an alternate definition?";
                configureReprompt(response);
            } else {
                response.setShouldEndSession(true);
            }
            outputSpeech.setText(speech);
        } else {
            outputSpeech.setText("No definition found for " + word);
        }

        return response;
    }

    private void configureReprompt(SpeechletResponse response) {
        Reprompt reprompt = new Reprompt();
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText("Would you like an alternate definition?");
        reprompt.setOutputSpeech(speech);
        response.setReprompt(reprompt);
        response.setShouldEndSession(false);
    }

    private int getDefinitionIndex(Session session, String wordKey) {
        int definitionIndex = 0;
        Object lastDefinition = session.getAttribute(wordKey);
        if (lastDefinition != null) {
            definitionIndex = Integer.parseInt(lastDefinition.toString()) + 1;
        }
        return definitionIndex;
    }
}
