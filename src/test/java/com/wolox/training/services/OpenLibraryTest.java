package com.wolox.training.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.BookDTO;
import com.wolox.training.service.OpenLibraryService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OpenLibraryTest {

    @Autowired
    private OpenLibraryService openLibraryService;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    private String body = "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"Leanne Graham\",\n" +
            "  \"username\": \"Bret\",\n" +
            "  \"email\": \"Sincere@april.biz\",\n" +
            "  \"address\": {\n" +
            "    \"street\": \"Kulas Light\",\n" +
            "    \"suite\": \"Apt. 556\",\n" +
            "    \"city\": \"Gwenborough\",\n" +
            "    \"zipcode\": \"92998-3874\",\n" +
            "    \"geo\": {\n" +
            "      \"lat\": \"-37.3159\",\n" +
            "      \"lng\": \"81.1496\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"phone\": \"1-770-736-8031 x56442\",\n" +
            "  \"website\": \"hildegard.org\",\n" +
            "  \"company\": {\n" +
            "    \"name\": \"Romaguera-Crona\",\n" +
            "    \"catchPhrase\": \"Multi-layered client-server neural-net\",\n" +
            "    \"bs\": \"harness real-time e-markets\"\n" +
            "  }\n" +
            "}";

   @Before
    public void setUp() throws JSONException {

       stubFor(get(urlEqualTo("/api/books?bibkeys=ISBN:0385472579&format=json&jscmd=data"))
               .willReturn(aResponse()
                       .withStatus(200)
                       .withHeader("Content-Type", "application/json")
                       .withBody(body)
               )
       );

   }

    @Test
    public void testOL() throws BookNotFoundException, JsonProcessingException {

        //RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8089/api/books?bibkeys=ISBN:0385472579&format=json&jscmd=data", String.class);
        //System.out.println(responseEntity);

        System.out.println(openLibraryService.getBook("0385472579"));
    }


}
