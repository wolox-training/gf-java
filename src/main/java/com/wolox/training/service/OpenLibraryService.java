package com.wolox.training.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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

        ObjectMapper objectMapper = new ObjectMapper();

        BookDTO bookDTO = new BookDTO();
        System.out.println(objectNode.get(isbnQueryValue));
        bookDTO.setIsbn(isbn);
        bookDTO.setTitle(objectMapper.convertValue(objectNode.get(isbnQueryValue).get("title"), String.class));
        bookDTO.setSubtitle(objectMapper.convertValue(objectNode.get(isbnQueryValue).get("subtitle"), String.class));
        bookDTO.setNumberOfPages(objectMapper.convertValue(objectNode.get(isbnQueryValue).get("number_of_pages"), String.class));
        bookDTO.setPublishDate(objectMapper.convertValue(objectNode.get(isbnQueryValue).get("publish_date"), String.class));
        bookDTO.setAuthors(objectMapper.convertValue(objectNode.get(isbnQueryValue).get("authors"), List.class));
        bookDTO.setPublisher(objectMapper.convertValue(objectNode.get(isbnQueryValue).get("publishers"), List.class));
        bookDTO.setPublisher(objectMapper.convertValue(objectNode.get(isbnQueryValue).get("publishers"), List.class));
        bookDTO.setCover(objectMapper.convertValue(objectNode.get(isbnQueryValue).get("cover").get("medium"), String.class));

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
