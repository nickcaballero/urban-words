package com.idklabs.alexa.urbanwords;

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
public class UrbanWordsResource {

    @Autowired
    private UrbanWordsService service;

    @RequestMapping("/day")
    public WordDefinition random() throws IOException {
        String wordOfTheDay = service.getWordOfTheDay();
        Optional<Definition> definition = service.getDefinition(wordOfTheDay)
                                                 .getDefinitions()
                                                 .stream()
                                                 .findFirst();
        if (definition.isPresent()) {
            return new WordDefinition(wordOfTheDay, definition.get()
                                                              .getDefinition());
        } else {
            return null;
        }
    }
}
