package com.wolox.training.controllers;

import com.wolox.training.exceptions.BookIdMismatchException;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
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

    /**
     * This method retrieve all books from database and showing them in json format
     * @return Iterable<Book> object to list all books
     */
    @GetMapping
    @ApiOperation(value = "Return all books", response = Book.class, responseContainer = "List")
    @ApiResponse(code = 200, message = "Successfully retrieved books")
    public Iterable<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    /**
     * This method retrieve a book filtering by id
     * @param id: Id of the book (Long)
     * @return the book with the id passed as parameter
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Giving an Id, return the book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    @ApiParam(name = "id", required = true)
    public Book findOne(@PathVariable Long id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    /**
     * This method retrieve a book filtering by book author
     * @param authorName: Name of the book author (String)
     * @return the book with the id passed as parameter
     */
    @GetMapping("/author/{authorName}")
    @ApiOperation(value = "Giving an author name, return the book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    @ApiParam(name = "authorName", required = true)
    public Book findByAuthor(@PathVariable String authorName) throws BookNotFoundException {
        return bookRepository.findByAuthor(authorName).orElseThrow(BookNotFoundException::new);
    }

    /**
     *  This method send a Book in JSON format to be saved in the database
     * @param book: The book that contains all the attributes to be saved (Book)
     * @return the saved Book in the database
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Giving a book, save it into database", response = Book.class)
    @ApiResponse(code = 201, message = "Successfully created book")
    @ApiParam(name = "book", required = true, format = "JSON")
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    /**
     * This method delete a Book in the database with the id passed by parameter
     * @param id: Id of the Book to be deleted (Long)
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Giving an id, delete a book from database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted book"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    @ApiParam(name = "id", required = true)
    public void delete(@PathVariable Long id) throws BookNotFoundException {
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    /**
     * This method updates a Book in the database validating if the id of the book you want to update
     * is the same as the one received within the book sent by parameter
     *
     * @param book: The Book with the attributes to be updated (Book)
     * @param id: The Id of the Book to be updated
     * @return the updated Book in the database
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Giving an id and a book, update a book in the database", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated book"),
            @ApiResponse(code = 404, message = "Book not found"),
            @ApiResponse(code = 409, message = "The book id mismatch")
    })
    @ApiParam(name = "id", required = true)
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) throws BookIdMismatchException, BookNotFoundException {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

}
