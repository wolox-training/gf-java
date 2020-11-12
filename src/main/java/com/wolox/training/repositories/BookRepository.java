package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface extends JpaRepository to provide us with all the methods that will serve us
 * for the persistence of Books
 *
 * @author Gabriel Fernandez
 * @version 1.0
 */
public interface BookRepository extends JpaRepository<Book, Long> {
    public Optional<Book> findByAuthor(String author);
    public Optional<Book> findByIsbn(String isbn);
}
