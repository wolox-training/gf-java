package com.wolox.training.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.BookDTO;
import com.wolox.training.service.OpenLibraryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OpenLibraryTest {

    @Autowired
    private OpenLibraryService openLibraryService;

    String openLibraryUrl = "/api/books";

    private WireMockServer wireMockServer;

    @Before
    public void setUp(){

        String isbn = "0385472579";
        String nonExistentIsbn = "";
        String url = openLibraryUrl + "?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data";
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();

       /* wireMockServer
                .stubFor(get(urlEqualTo(url))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(200)
                                .withBodyFile("{}")));*/

        wireMockServer
                .stubFor(get(urlEqualTo(url))
                        .willReturn(aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(200)
                                .withBodyFile("ol_response.json")));



    }

    @After
    public void tearDown(){
        wireMockServer.stop();
    }

    @Test
    public void testOL() throws BookNotFoundException, JsonProcessingException {

    BookDTO bookDTO = openLibraryService.getBook("0385472579");

    }


}
