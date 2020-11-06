package com.wolox.training.controllers;

import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.exceptions.IdMismatchException;
import com.wolox.training.exceptions.NotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public Iterable<User> getAllBooks(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id){
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping("/username/{username}")
    public User findByUsername(@PathVariable String username){
        return userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.findById(id).orElseThrow(NotFoundException::new);
        userRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user.getId() != id) {
            throw new IdMismatchException();
        }
        userRepository.findById(id).orElseThrow(NotFoundException::new);
        return userRepository.save(user);
    }

    @PutMapping("/{userId}/{bookId}")
    public User addBookToUser(@PathVariable Long userId, @PathVariable Long bookId) throws BookAlreadyOwnedException {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        user.addBook(book);
        return userRepository.save(user);
    }

    @DeleteMapping("/{userId}/{bookId}")
    public User removeBookToUser(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        user.removeBook(book);
        return userRepository.save(user);
    }
}
