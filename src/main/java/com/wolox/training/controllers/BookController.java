package com.wolox.training.controllers;

import com.wolox.training.exceptions.IdMismatchException;
import com.wolox.training.exceptions.NotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * This class is a rest controller for book.
 * It concentrates the handling of http requests that retrieve different
 * types of book information in JSON format
 *
 * @author Gabriel Fernandez
 * @version 1.0
 */
@RestController
@RequestMapping("/api/books")
@Api
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    @ApiOperation(value = "Return all books", response = Book.class)
    @ApiResponse(code = 200, message = "Successfully retrieved books")
    public Iterable<Book> getAllBooks(){
        return bookRepository.findAll();
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Giving an Id, return the book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public Book findOne(@PathVariable Long id) throws NotFoundException {
        return bookRepository.findById(id).orElseThrow(NotFoundException::new);
    }


    @GetMapping("/author/{authorName}")
    @ApiOperation(value = "Giving an author name, return the book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public Book findByAuthor(@PathVariable String authorName) throws NotFoundException {
        return bookRepository.findByAuthor(authorName).orElseThrow(NotFoundException::new);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Giving a book, save it into database", response = Book.class)
    @ApiResponse(code = 201, message = "Successfully created book")
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Giving an id, delete a book from database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted book"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public void delete(@PathVariable Long id) throws NotFoundException {
        bookRepository.findById(id).orElseThrow(NotFoundException::new);
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Giving an id and a book, update a book in the database", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated book"),
            @ApiResponse(code = 404, message = "Book not found"),
            @ApiResponse(code = 409, message = "The book id mismatch")
    })
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) throws IdMismatchException, NotFoundException {
        if (book.getId() != id) {
            throw new IdMismatchException();
        }
        bookRepository.findById(id).orElseThrow(NotFoundException::new);
        return bookRepository.save(book);
    }

}
