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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        testBook = new Book();
        testOtherBook = new Book();

        testBook.setImage("image");
        testBook.setPublisher("pub");
        testBook.setAuthor("author");
        testBook.setYear("2000");
        testBook.setPages(200);
        testBook.setSubtitle("subtitle");
        testBook.setTitle("title");
        testBook.setIsbn("123456789");
        testBook.setGenre("genre");

        testOtherBook.setImage("image2");
        testOtherBook.setPublisher("pub2");
        testOtherBook.setAuthor("author2");
        testOtherBook.setYear("2000");
        testOtherBook.setPages(200);
        testOtherBook.setSubtitle("subtitle2");
        testOtherBook.setTitle("title2");
        testOtherBook.setIsbn("987654321");
        testOtherBook.setGenre("genre2");

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
        String publisher = "pub2";
        String genre = "genre2";
        String year = "2000";
        List<Book> testList = bookRepository.findAllByPublisherAndGenreAndYear(publisher, genre, year, null);
        assertFalse(testList.isEmpty());
        assertTrue(testList.contains(testOtherBook));
    }

    @Test
    public void givingNonExistentData_whenGetBookByPubGenYear_thenReturnEmptyList(){
        String publisher = "Editoral Libertador";
        String genre = "Science";
        String year = "2000";
        List<Book> testList = bookRepository.findAllByPublisherAndGenreAndYear(publisher, genre, year, null);
        assertTrue(testList.isEmpty());
    }

    @Test
    public void whenGetBookByPubGenYearQuery_thenReturnAList(){
        String publisher = "pub2";
        String year = "2000";
        List<Book> testList = bookRepository.findAllByPublisherAndGenreAndYear(publisher, null, null, null);
        assertFalse(testList.isEmpty());
        assertTrue(testList.contains(testOtherBook));
    }

    @Test
    public void whenGetAllBooksWhitFilters_thenReturnAList(){
        String publisher = "pub2";
        String year = "2000";
        List<Book> testList = bookRepository.findAll(null, null, null, null, null, publisher, year, null, null, null);
        assertFalse(testList.isEmpty());
        assertTrue(testList.contains(testOtherBook));
    }

    @Test
    public void whenGetAllBooksWhitFiltersAndPagingSorting_thenReturnAList(){
        String year = "2000";
        List<Book> testList = bookRepository.findAll(null, null, null, null, null, null, year, null, null, PageRequest.of(0,2, Sort.by("title")));
        assertFalse(testList.isEmpty());
        assertEquals(testBook.getTitle(), testList.get(0).getTitle());
        assertEquals(testOtherBook.getTitle(), testList.get(1).getTitle());
    }


}
