package com.wolox.training.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * This class represent a book with its respective attributes
 *
 * @author Gabriel Fernandez
 * @version 1.0
 */
@Entity
public class Book {

    /**
     * The Id of a Book
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The genre of a Book
     */
    @Column
    @NotNull
    private String genre;

    /**
     * The author of a Book
     */
    @Column
    @NotNull
    private String author;

    /**
     * The cover image of a Book
     */
    @Column
    @NotNull
    private String image;

    /**
     * The title of a Book
     */
    @Column
    @NotNull
    private String title;

    /**
     * The subtitle of a Book
     */
    @Column
    @NotNull
    private String subtitle;

    /**
     * The publisher of a Book
     */
    @Column
    @NotNull
    private String publisher;

    /**
     * The year of publication of a Book
     */
    @Column
    @NotNull
    private String year;

    /**
     * The pages of a Book
     */
    @Column
    @NotNull
    private Integer pages;

    /**
     * The ISBN of a Book
     */
    @Column
    @NotNull
    private String isbn;

    public Book() {

    }

    public Book(String genre, String author, String image, String title, String subtitle, String publisher, String year, Integer pages, String isbn) {
        this.genre = genre;
        this.author = author;
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.isbn = isbn;
    }

    public long getId(){
        return this.id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        String value = " ";
        try {
            value = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value;
    }
}
