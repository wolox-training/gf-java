package com.wolox.training.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
public class BookDTO {

    private String isbn;
    private String title;
    private String subtitle;
    private List<HashMap<String, String>> publishers;
    @JsonProperty("publish_date")
    private String publishDate;
    @JsonProperty("number_of_pages")
    private int numberOfPages;
    private List<HashMap<String, String>> authors;
    private HashMap<String, String> cover;

    public Book toBook(){
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setSubtitle(subtitle);
        book.setPages(numberOfPages);
        book.setYear(publishDate);
        book.setAuthor(authors.get(0).get("name"));
        book.setPublisher(publishers.get(0).get("name"));
        book.setImage(cover.get("small"));

        return book;
    }
}
