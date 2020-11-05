package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    public Book findByAuthor(String author);
}
