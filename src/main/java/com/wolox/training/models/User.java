package com.wolox.training.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Preconditions;
import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.exceptions.NotFoundException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name = "Users")
@ApiModel(description = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ApiModelProperty(notes = "The username")
    private String username;

    @NotNull
    @ApiModelProperty(notes = "The name of a user")
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @NotNull
    @ApiModelProperty(notes = "The birthdate of a user")
    private LocalDate birthdate;

    @ManyToMany(cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "The list of books owned by a user")
    private List<Book> books = new ArrayList<>();

    public User(){

    }

    public User(@NotNull String username, @NotNull String name, @NotNull LocalDate birthdate) {
        this.username = username;
        this.name = name;
        this.birthdate = birthdate;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Preconditions.checkNotNull(username, "The username can not be null");
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name, "The name can not be null");
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        Preconditions.checkNotNull(birthdate, "The birthdate can not be null");
        this.birthdate = birthdate;
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(this.books);
    }

    public void setBooks(List<Book> books) {
        Preconditions.checkNotNull(books, "The list of books can not be null");
        this.books = books;
    }

    public void addBook(Book book) throws BookAlreadyOwnedException {
        Preconditions.checkNotNull(book, "The book can not be null");
        if(this.books.contains(book)){
            throw new BookAlreadyOwnedException("The User already owns the Book");
        }
        this.books.add(book);
    }

    public void removeBook(Book book) {
        Preconditions.checkNotNull(book, "The book can not be null");
        if(!(this.books.contains(book))){
            throw new NotFoundException("The User has not the Book you try to remove");
        }
        this.books.remove(book);
    }
}
