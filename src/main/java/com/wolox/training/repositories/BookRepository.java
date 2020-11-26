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

   @Query("SELECT b FROM Book b " +
           "WHERE (:genre is null OR b.genre = :genre) " +
           "AND (:author is null OR b.author = :author) " +
           "AND (:image is null OR b.image = :image) " +
           "AND (:title is null OR b.title = :title) " +
           "AND (:subtitle is null OR b.subtitle = :subtitle) " +
           "AND (:publisher is null OR b.publisher = :publisher) " +
           "AND (:year is null OR b.year = :year) " +
           "AND (:pages is null OR b.pages = :pages) " +
           "AND (:isbn is null OR b.isbn = :isbn)")
   public List<Book> findAll(
           @Param("genre") String genre,
           @Param("author") String author,
           @Param("image") String image,
           @Param("title") String title,
           @Param("subtitle") String subtitle,
           @Param("publisher") String publisher,
           @Param("year") String year,
           @Param("pages") Integer pages,
           @Param("isbn") String isbn);

}
