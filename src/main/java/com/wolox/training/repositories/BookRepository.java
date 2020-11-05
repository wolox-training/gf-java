package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    public Optional<Book> findByAuthor(String author);
}
