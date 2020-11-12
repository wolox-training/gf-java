package com.wolox.training.models;

import java.util.HashMap;
import java.util.List;

public class BookDTO {

    private String isbn;
    private String title;
    private String subtitle;
    private List<HashMap<String, String>> publisher;
    private String publishDate;
    private String numberOfPages;
    private List<HashMap<String, String>> authors;

    public BookDTO() {
    }

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

    public List<HashMap<String, String>> getPublisher() {
        return publisher;
    }

    public void setPublisher(List<HashMap<String, String>> publisher) {
        this.publisher = publisher;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public List<HashMap<String, String>> getAuthors() {
        return authors;
    }

    public void setAuthors(List<HashMap<String, String>> authors) {
        this.authors = authors;
    }

    public Book toBook(){
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setSubtitle(subtitle);
        book.setPages(Integer.parseInt(numberOfPages));
        book.setYear(publishDate);
        book.setAuthor(authors.get(0).get("name"));
        book.setPublisher(publisher.get(0).get("name"));
        book.setImage("---");

        return book;
    }
}
