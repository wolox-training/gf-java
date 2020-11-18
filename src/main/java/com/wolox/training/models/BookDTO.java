package com.wolox.training.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<HashMap<String, String>> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<HashMap<String, String>> publishers) {
        this.publishers = publishers;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public List<HashMap<String, String>> getAuthors() {
        return authors;
    }

    public void setAuthors(List<HashMap<String, String>> authors) {
        this.authors = authors;
    }

    public HashMap<String, String> getCover() {
        return cover;
    }

    public void setCover(HashMap<String, String> cover) {
        this.cover = cover;
    }

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
