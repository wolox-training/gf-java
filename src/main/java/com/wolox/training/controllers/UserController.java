package com.wolox.training.controllers;

import com.wolox.training.exceptions.*;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

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
    public List<User> getAllBooks(Pageable pageable){
        return userRepository.findAllUsers(pageable);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Giving an Id, return the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public User findOne(@ApiParam(name = "id", required = true)@PathVariable Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/username/{username}")
    @ApiOperation(value = "Giving a username, return the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public User findByUsername(@ApiParam(name = "username", required = true)@PathVariable String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Giving a user, save it into database", response = User.class)
    @ApiResponse(code = 201, message = "Successfully created user")
    public User create(@ApiParam(name = "user", required = true)@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Giving an Id, delete an user from database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public void delete(@ApiParam(name = "id", required = true)@PathVariable Long id) throws UserNotFoundException {
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
    public User updateUser(@ApiParam(name = "book", required = true)@RequestBody User user, @ApiParam(name = "id", required = true)@PathVariable Long id) throws UserIdMismatchException, UserNotFoundException {
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
    public User addBookToUser(@ApiParam(name = "userId", required = true)@PathVariable Long userId, @ApiParam(name = "bookId", required = true)@PathVariable Long bookId) throws BookAlreadyOwnedException, UserNotFoundException, BookNotFoundException {
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
    public User removeBookToUser(@ApiParam(name = "userId", required = true)@PathVariable Long userId, @ApiParam(name = "bookId", required = true)@PathVariable Long bookId) throws UserNotFoundException, BookNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        user.removeBook(book);
        return userRepository.save(user);
    }

    @GetMapping("/username")
    public User getCurrentUser(Principal principal) throws UserNotFoundException {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        return user;
    }

    @PutMapping("/{id}/newPassword")
    public User newPassword(@PathVariable Long id, @RequestBody Map<String, String> parameters) throws UserNotFoundException, OldPasswordMismatchEception {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        String oldPass = parameters.get("oldPass");
        String newPass = parameters.get("newPass");

        if (!BCrypt.checkpw(oldPass, user.getPassword())){
            throw new OldPasswordMismatchEception("The old password mismatch");
        }
        else {
            user.setPassword(newPass);
        }

        return user;
    }
}
