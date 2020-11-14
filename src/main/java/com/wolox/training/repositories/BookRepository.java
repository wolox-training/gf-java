package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

   @Query("SELECT b FROM Book b " +
            "WHERE (:publisher is null OR b.publisher = :publisher) " +
            "AND (:genre is null OR b.genre = :genre) " +
            "AND (:year is null OR b.year = :year)")
    public List<Book> findAllByPublisherAndGenreAndYear(
            @Param("publisher") String publisher,
            @Param("genre") String genre,
            @Param("year") String year);

}
