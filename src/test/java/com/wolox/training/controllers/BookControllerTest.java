package com.wolox.training.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.models.BookDTO;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.security.CustomAuthenticationProvider;
import com.wolox.training.service.OpenLibraryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.swing.plaf.PanelUI;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private OpenLibraryService openLibraryService;

    @MockBean
    private CustomAuthenticationProvider authenticationProvider;

    private String url;
    private Book book1;
    private Book book2;
    private List<Book> books;
    private BookDTO bookDTO;

    @Before
    public void setUp() throws BookNotFoundException {
        url = "/api/books";
        book1 = new Book("Philosophy", "Tres Iniciados", "image123.jpg", "El Kybalion",
                "---", "Editorial Pluma y Papel", "1908", 200, "978-987-684-143-4");
        book2 = new Book("Science", "Charles Darwin", "image234.jpg", "On the Origin of Species",
                "---", "Editoral Libertador", "1859", 500, "789-285-624-843-6");
        books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        bookDTO = new BookDTO();
        bookDTO.setIsbn("789-285-624-843-6");
        bookDTO.setTitle("Title");
        bookDTO.setSubtitle("Subtitle");
        bookDTO.setPublishDate("2000");
        bookDTO.setNumberOfPages("200");
        //bookDTO.setAuthors(new ArrayList<>());
        //bookDTO.setPublisher(new ArrayList<>());
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Publisher");
        bookDTO.setPublisher(Collections.singletonList(map));

        map.clear();
        map.put("name", "Author");
        bookDTO.setAuthors(Collections.singletonList(map));

        given(bookRepository.findAll()).willReturn(books);
        given(bookRepository.findById(1L)).willReturn(java.util.Optional.of(book1));
        given(bookRepository.findById(0L)).willReturn(java.util.Optional.of(book2));
        given(bookRepository.findByIsbn("978-987-684-143-4")).willReturn(java.util.Optional.of(book1));
        given(openLibraryService.getBook("789-285-624-843-6")).willReturn(bookDTO);
        given(openLibraryService.getBook("7")).willThrow(new BookNotFoundException("The Book is Not Found"));
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void whenGetBooks_thenReturnJsonArray() throws Exception {
        mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(books.size())))
                .andExpect(jsonPath("$[1].isbn", is(book2.getIsbn())));
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenId_whenGetBook_thenReturnJson() throws Exception {
        mvc.perform(get(url + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", is(book1.getIsbn())));
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenNonExistentId_whenGetBook_thenReturnStatus404() throws Exception {
        mvc.perform(get(url + "/100").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenJsonBook_whenCreateBook_thenReturnStatus201() throws Exception {
        String contentBook = new ObjectMapper().writeValueAsString(book1);
        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(contentBook))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenJsonBookAndId_whenUpdateBook_thenReturnStatus200() throws Exception {
        String contentBook = new ObjectMapper().writeValueAsString(book2);
        mvc.perform(put(url + "/0").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(contentBook))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenJsonBookAndMismatchId_whenUpdateBook_thenReturnStatus409() throws Exception {
        String contentBook = new ObjectMapper().writeValueAsString(book2);
        mvc.perform(put(url + "/1").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(contentBook))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenId_whenDeleteBook_thenReturnStatus200() throws Exception {
        mvc.perform(delete(url + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenNonExistentId_whenDeleteBook_thenReturnStatus404() throws Exception {
        mvc.perform(delete(url + "/100").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenNotAuthenticated_thenReturnStatus401() throws Exception {
        mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenIsbn_whenGetBookFromDatabase_thenReturnStatus200() throws Exception {
        mvc.perform(get(url + "/isbn/978-987-684-143-4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book1.getTitle())));
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenIsbn_whenGetBookFromOpenLibrary_thenReturnStatus201() throws Exception {
        mvc.perform(get(url + "/isbn/789-285-624-843-6").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenNonExistentIsbn_whenGetBook_thenReturnStatus201() throws Exception {
        mvc.perform(get(url + "/isbn/7").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
