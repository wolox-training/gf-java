package com.wolox.training.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class OpenLibraryService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${OpenLibraryUrl}")
    String openLibraryUrl;

    private BookDTO getBookNode(ObjectNode objectNode, String isbn) throws JsonProcessingException {
        final String isbnQueryValue = "ISBN:" + isbn;

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        BookDTO bookDTO = objectMapper.treeToValue(objectNode.get(isbnQueryValue), BookDTO.class);
        bookDTO.setIsbn(isbn);

        return bookDTO;
    }

    public BookDTO getBook(String isbn) throws BookNotFoundException, JsonProcessingException {
        final String isbnQueryValue = "ISBN:" + isbn;
        URI uri = UriComponentsBuilder
                .fromHttpUrl(openLibraryUrl)
                .path("books")
                .queryParam("bibkeys", "ISBN:" + isbn)
                .queryParam("format", "json")
                .queryParam("jscmd", "data")
                .build()
                .toUri();

        ObjectNode objectNode = restTemplate.getForObject(uri, ObjectNode.class);

        if (!objectNode.isEmpty()){

            BookDTO bookDTO = getBookNode(objectNode, isbn);
            return bookDTO;

        }else {
            throw new BookNotFoundException("The Book is Not Found");
        }
    }

}
