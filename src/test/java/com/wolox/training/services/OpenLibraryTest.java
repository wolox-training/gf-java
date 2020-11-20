package com.wolox.training.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.BookDTO;
import com.wolox.training.service.OpenLibraryService;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OpenLibraryTest {

    @Autowired
    private OpenLibraryService openLibraryService;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Before
    public void setUp() throws JSONException {

        stubFor(get(urlEqualTo("/api/books?bibkeys=ISBN:0385472579&format=json&jscmd=data"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("ol_response.json")
                )
        );

        stubFor(get(urlEqualTo("/api/books?bibkeys=ISBN:0385400000&format=json&jscmd=data"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{}")
                )
        );

    }

    @Test
    public void givenExistentIsbn_whenGetBookDTO_thenReturnCorrectBook() throws BookNotFoundException, JsonProcessingException {
        String coverResponse = "https://covers.openlibrary.org/b/id/240726-S.jpg";
        String authorResponse = "Zhizhong Cai";
        int pagesResponse = 159;
        BookDTO testBookDTO = openLibraryService.getBook("0385472579");
        assertFalse(testBookDTO == null);
        assertEquals(testBookDTO.getCover().get("small"), coverResponse);
        assertEquals(testBookDTO.getAuthors().get(0).get("name"), authorResponse);
        assertEquals(testBookDTO.getNumberOfPages(), pagesResponse);
    }

    @Test(expected = BookNotFoundException.class)
    public void givenNonExistentIsbn_whenGetBookDTO_thenThrowException() throws BookNotFoundException, JsonProcessingException {
        String coverResponse = "https://covers.openlibrary.org/b/id/240726-S.jpg";
        String authorResponse = "Zhizhong Cai";
        int pagesResponse = 159;
        BookDTO testBookDTO = openLibraryService.getBook("0385400000");
        assertTrue(testBookDTO == null);
    }

}
