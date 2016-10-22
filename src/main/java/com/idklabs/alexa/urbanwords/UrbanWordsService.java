package com.idklabs.alexa.urbanwords;

import com.idklabs.alexa.urbanwords.api.DefinitionResponse;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * @author nick.caballero
 */
@Service
public class UrbanWordsService {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Get the word of the day as defined by Urban Dictionary
     * 
     * @return The word of the day
     */
    public String getWordOfTheDay() throws IOException {
        return Jsoup.connect(UriComponentsBuilder.fromHttpUrl("http://www.urbandictionary.com")
                                                 .build()
                                                 .toUriString())
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get()
                    .select(".def-header")
                    .first()
                    .select("a.word")
                    .first()
                    .text();
    }

    /**
     * Get the definition for a word as defined by Urban Dictionary
     *
     * @param word The word to define
     * @return The definition response
     */
    public DefinitionResponse getDefinition(String word) {
        return restTemplate.getForObject(
                        UriComponentsBuilder.fromHttpUrl("http://api.urbandictionary.com/v0/define")
                                            .queryParam("term", word)
                                            .build()
                                            .toUriString(),
                        DefinitionResponse.class);
    }
}
