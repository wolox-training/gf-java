package com.wolox.training.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * This class represent a book with its respective attributes
 *
 * @author Gabriel Fernandez
 * @version 1.0
 */
@Entity
@ApiModel(description = "Books")
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
    @ApiModelProperty(notes = "The book genre: could be horror, comedy, drama, etc.")
    private String genre;

    /**
     * The author of a Book
     */
    @Column
    @NotNull
    @ApiModelProperty(notes = "The book author")
    private String author;

    /**
     * The cover image of a Book
     */
    @Column
    @NotNull
    @ApiModelProperty(notes = "The cover image of a Book")
    private String image;

    /**
     * The title of a Book
     */
    @Column
    @NotNull
    @ApiModelProperty(notes = "The book title")
    private String title;

    /**
     * The subtitle of a Book
     */
    @Column
    @NotNull
    @ApiModelProperty(notes = "The book subtitle")
    private String subtitle;

    /**
     * The publisher of a Book
     */
    @Column
    @NotNull
    @ApiModelProperty(notes = "The publisher of a book")
    private String publisher;

    /**
     * The year of publication of a Book
     */
    @Column
    @NotNull
    @ApiModelProperty(notes = "The year of publication of a book")
    private String year;

    /**
     * The pages of a Book
     */
    @Column
    @NotNull
    @ApiModelProperty(notes = "The number of pages in the book")
    private Integer pages;

    /**
     * The ISBN of a Book
     */
    @Column
    @NotNull
    @ApiModelProperty(notes = "The book ISBN")
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
        Preconditions.checkNotNull(author, "The author can not be null");
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        Preconditions.checkNotNull(image, "The image path can not be null");
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Preconditions.checkNotNull(title, "The title can not be null");
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        Preconditions.checkNotNull(subtitle, "The subtitle can not be null");
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        Preconditions.checkNotNull(publisher, "The publisher can not be null");
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        Preconditions.checkNotNull(year, "The year can not be null");
        Preconditions.checkArgument(Integer.parseInt(year) < LocalDate.now().getYear(),
                "The year can not be greater than " + LocalDate.now().getYear());
        this.year = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        Preconditions.checkNotNull(pages, "The number of pages can not be null");
        Preconditions.checkArgument(pages > 0, "The number of pages must be greater than 0");
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        Preconditions.checkNotNull(isbn, "The ISBN can not be null");
        this.isbn = isbn;
    }

}
