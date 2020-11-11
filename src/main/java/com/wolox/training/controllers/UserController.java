package com.wolox.training.controllers;

import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.exceptions.UserIdMismatchException;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.UserNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

@RestController
@RequestMapping("/api/users")
@Api
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    @ApiOperation(value = "Return all users", response = User.class, responseContainer = "List")
    @ApiResponse(code = 200, message = "Successfully retrieved books")
    public Iterable<User> getAllBooks(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Giving an Id, return the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @ApiParam(name = "id", required = true)
    public User findOne(@PathVariable Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/username/{username}")
    @ApiOperation(value = "Giving a username, return the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @ApiParam(name = "username", required = true)
    public User findByUsername(@PathVariable String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Giving a user, save it into database", response = User.class)
    @ApiResponse(code = 201, message = "Successfully created user")
    @ApiParam(name = "user", required = true, format = "JSON")
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Giving an Id, delete an user from database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @ApiParam(name = "id", required = true)
    public void delete(@PathVariable Long id) throws UserNotFoundException {
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Giving an id and a user, update a book in the database", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 409, message = "The user id mismatch")
    })
    @ApiParam(name = "id", required = true)
    public User updateUser(@RequestBody User user, @PathVariable Long id) throws UserIdMismatchException, UserNotFoundException {
        if (user.getId() != id) {
            throw new UserIdMismatchException();
        }
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }

    @PutMapping("/{userId}/{bookId}")
    @ApiOperation(value = "Giving a user id and a book id, add a book to the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added book"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 404, message = "Book not found"),
            @ApiResponse(code = 208, message = "The book is already owned")
    })
    @ApiParam(name = "userId", required = true)
    public User addBookToUser(@PathVariable Long userId, @PathVariable Long bookId) throws BookAlreadyOwnedException, UserNotFoundException, BookNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        user.addBook(book);
        return userRepository.save(user);
    }

    @DeleteMapping("/{userId}/{bookId}")
    @ApiOperation(value = "Giving a user id and a book id, remove a book from the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted book"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 404, message = "Book not found"),
            @ApiResponse(code = 404, message = "The User has not the Book you try to remove")
    })
    @ApiParam(name = "userId", required = true)
    public User removeBookToUser(@PathVariable Long userId, @PathVariable Long bookId) throws UserNotFoundException, BookNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        user.removeBook(book);
        return userRepository.save(user);
    }
}
