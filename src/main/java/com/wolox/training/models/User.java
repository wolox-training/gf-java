package com.wolox.training.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.common.base.Preconditions;
import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.exceptions.BookNotFoundException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name = "Users")
@ApiModel(description = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ApiModelProperty(notes = "The username")
    private String username;

    @NotNull
    @ApiModelProperty(notes = "The user password")
    private String password;

    @NotNull
    @ApiModelProperty(notes = "The name of a user")
    private String name;

    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @ApiModelProperty(notes = "The birthdate of a user")
    private LocalDate birthdate;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @ApiModelProperty(notes = "The list of books owned by a user")
    private List<Book> books = new ArrayList<>();

    public void setUsername(String username) {
        Preconditions.checkNotNull(username, "The username can not be null");
        this.username = username;
    }

    public void setPassword(String password) {
        Preconditions.checkNotNull(password, "The password can not be null");
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name, "The name can not be null");
        this.name = name;
    }

    public void setBirthdate(LocalDate birthdate) {
        Preconditions.checkNotNull(birthdate, "The birthdate can not be null");
        this.birthdate = birthdate;
    }

    public void setBooks(List<Book> books) {
        Preconditions.checkNotNull(books, "The list of books can not be null");
        this.books = books;
    }

    public void addBook(Book book) throws BookAlreadyOwnedException {
        Preconditions.checkNotNull(book, "The book can not be null");
        if(books.contains(book)){
            throw new BookAlreadyOwnedException("The User already owns the Book");
        }
        books.add(book);
    }

    public void removeBook(Book book) throws BookNotFoundException {
        Preconditions.checkNotNull(book, "The book can not be null");
        if(!books.contains(book)){
            throw new BookNotFoundException("The User has not the Book you try to remove");
        }
        books.remove(book);
    }
}
