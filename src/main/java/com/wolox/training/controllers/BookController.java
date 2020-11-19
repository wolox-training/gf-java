package com.wolox.training.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wolox.training.exceptions.BookIdMismatchException;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.models.BookDTO;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.service.OpenLibraryService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private OpenLibraryService openLibraryService;

    @GetMapping
    @ApiOperation(value = "Return all books", response = Book.class, responseContainer = "List")
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
    public Book findOne(@ApiParam(name = "id", required = true)@PathVariable Long id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }


    @GetMapping("/author/{authorName}")
    @ApiOperation(value = "Giving an author name, return the book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public Book findByAuthor(@ApiParam(name = "authorName", required = true)@PathVariable String authorName) throws BookNotFoundException {
        return bookRepository.findByAuthor(authorName).orElseThrow(BookNotFoundException::new);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> findByIsbn(@PathVariable String isbn) throws BookNotFoundException, JsonProcessingException {

        Optional<Book> oBook = bookRepository.findByIsbn(isbn);

        if (oBook.isPresent()){
            return new ResponseEntity<>(oBook.get(), HttpStatus.OK);
        } else {
            BookDTO bookDTO = openLibraryService.getBook(isbn);
            Book book = bookRepository.save(bookDTO.toBook());
            return new ResponseEntity<>(book, HttpStatus.CREATED);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Giving a book, save it into database", response = Book.class)
    @ApiResponse(code = 201, message = "Successfully created book")
    public Book create(@ApiParam(name = "book", required = true)@RequestBody Book book) {
        return bookRepository.save(book);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Giving an id, delete a book from database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted book"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public void delete(@ApiParam(name = "id", required = true)@PathVariable Long id) throws BookNotFoundException {
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Giving an id and a book, update a book in the database", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated book"),
            @ApiResponse(code = 404, message = "Book not found"),
            @ApiResponse(code = 409, message = "The book id mismatch")
    })
    public Book updateBook(@ApiParam(name = "book", required = true)@RequestBody Book book, @ApiParam(name = "id", required = true)@PathVariable Long id) throws BookIdMismatchException, BookNotFoundException {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

}
