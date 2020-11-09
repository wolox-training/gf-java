package com.wolox.training.controllers;

import com.wolox.training.exceptions.IdMismatchException;
import com.wolox.training.exceptions.NotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
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
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    /**
     * This method returns views concatenating "Hello" + the name that we send by parameter
     * @param name: The name to be concatenated in the views
     * @param model
     * @return views named greeting.html
     */
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    /**
     * This method retrieve all books from database and showing them in json format
     * @return Iterable<Book> object to list all books
     */
    @GetMapping
    public Iterable<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    /**
     * This method retrieve a book filtering by id
     * @param id: Id of the book (Long)
     * @return the book with the id passed as parameter
     */
    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) throws NotFoundException {
        return bookRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    /**
     * This method retrieve a book filtering by book author
     * @param authorName: Name of the book author (String)
     * @return the book with the id passed as parameter
     */
    @GetMapping("/author/{authorName}")
    public Book findByAuthor(@PathVariable String authorName) throws NotFoundException {
        return bookRepository.findByAuthor(authorName).orElseThrow(NotFoundException::new);
    }

    /**
     *  This method send a Book in JSON format to be saved in the database
     * @param book: The book that contains all the attributes to be saved (Book)
     * @return the saved Book in the database
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    /**
     * This method delete a Book in the database with the id passed by parameter
     * @param id: Id of the Book to be deleted (Long)
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws NotFoundException {
        bookRepository.findById(id).orElseThrow(NotFoundException::new);
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
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) throws IdMismatchException, NotFoundException {
        if (book.getId() != id) {
            throw new IdMismatchException();
        }
        bookRepository.findById(id).orElseThrow(NotFoundException::new);
        return bookRepository.save(book);
    }

}
