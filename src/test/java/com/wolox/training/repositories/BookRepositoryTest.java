package com.wolox.training.repositories;

import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Book testBook;
    private List<Book> testBooks;
    private Book testOtherBook;

    @Before
    public void setUp(){
        testBook = new Book("Philosophy", "Tres Iniciados", "image123.jpg", "El Kybalion",
                "---", "Editorial Pluma y Papel", "1908", 200, "978-987-684-143-4");
        testOtherBook = new Book("Science", "Charles Darwin", "image234.jpg", "On the Origin of Species",
                "---", "Editoral Libertador", "1859", 500, "789-285-624-843-6");
        testBooks = new ArrayList<>();
        testBooks.add(testBook);
        testBooks.add(testOtherBook);
        entityManager.persist(testBook);
        entityManager.persist(testOtherBook);
        entityManager.flush();
    }

    @Test
    public void whenFindAll_thenReturnAllBooks(){
        List<Book> repositoryList = bookRepository.findAll();
        assertEquals(repositoryList.size(), testBooks.size());
        assertEquals(repositoryList.get(0).getAuthor(), testBooks.get(0).getAuthor());
        assertEquals(repositoryList.get(1).getIsbn(), testBooks.get(1).getIsbn());
    }

    @Test(expected = ConstraintViolationException.class)
    public void whenCreateBookWithoutAuthor_thenThrowException(){
        Book book = new Book("Philosophy", null, "image123.jpg", "El Kybalion",
                "---", "Editorial Pluma y Papel", "1908", 200, "978-987-684-143-4");
        bookRepository.save(book);
    }

    @Test
    public void whenSaveBook_thenReturnBook(){
        Book repositoryBook = bookRepository.save(testBook);
        assertEquals(testBook.getIsbn(), repositoryBook.getIsbn());
    }

    @Test
    public void whenFindBook_thenReturnExpectedBook() throws BookNotFoundException {
        Book repositoryBook = bookRepository.findByAuthor(testBook.getAuthor()).orElseThrow(BookNotFoundException::new);
        assertEquals(testBook.getTitle(), repositoryBook.getTitle());
    }

    @Test(expected = BookNotFoundException.class)
    public void givenNotExistingId_whenFindBook_thenThrowException() throws BookNotFoundException {
       Book repoBook = bookRepository.findById(100L).orElseThrow(BookNotFoundException::new);
    }

    @Test
    public void whenDeleteBook_thenNotAppearInFindAll(){
        bookRepository.delete(testBook);
        assertFalse(bookRepository.findAll().contains(testBook));
        assertTrue(bookRepository.findAll().contains(testOtherBook));
    }

    @Test
    public void whenGetBookByPubGenYear_thenReturnAList(){
        String publisher = "Editoral Libertador";
        String genre = "Science";
        String year = "1859";
        List<Book> testList = bookRepository.findAllByPublisherAndGenreAndYear(publisher, genre, year);
        assertFalse(testList.isEmpty());
        assertTrue(testList.contains(testOtherBook));
    }

    @Test
    public void givingNonExistentData_whenGetBookByPubGenYear_thenReturnEmptyList(){
        String publisher = "Editoral Libertador";
        String genre = "Science";
        String year = "2000";
        List<Book> testList = bookRepository.findAllByPublisherAndGenreAndYear(publisher, genre, year);
        assertTrue(testList.isEmpty());
    }


}
