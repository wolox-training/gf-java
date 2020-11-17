package com.wolox.training.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import com.wolox.training.security.CustomAuthenticationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CustomAuthenticationProvider authenticationProvider;

    private String url;
    private Book book1;
    private Book book2;
    private User user1;
    private User user2;
    private List<User> users;
    private Map<String, String> passwords;

    @Before
    public void setUp(){
        url = "/api/users";
        book1 = new Book("Philosophy", "Tres Iniciados", "image123.jpg", "El Kybalion",
                "---", "Editorial Pluma y Papel", "1908", 200, "978-987-684-143-4");
        book2 = new Book("Science", "Charles Darwin", "image234.jpg", "On the Origin of Species",
                "---", "Editoral Libertador", "1859", 500, "789-285-624-843-6");

        user1 = new User("gabriel","123456" , "Gabriel Fernandez", LocalDate.of(2000, 01, 26));
        user2 = new User("TestUsername","1234" , "Gabriela", LocalDate.of(2005, 10, 15));

        try {
            user1.addBook(book2);
        } catch (BookAlreadyOwnedException e) {
            e.printStackTrace();
        }

        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        passwords = new HashMap<>();
        passwords.put("newPass", "123456789");
        passwords.put("oldPass", "123456");


        given(userRepository.findAllUsers(PageRequest.of(0,20))).willReturn(users);
        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user1));
        given(userRepository.findById(0L)).willReturn(java.util.Optional.of(user2));
        given(bookRepository.findById(1L)).willReturn(java.util.Optional.of(book1));
        given(bookRepository.findById(2L)).willReturn(java.util.Optional.of(book2));
        given(userRepository.findByUsername("gabriel")).willReturn(java.util.Optional.ofNullable(user1));
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givingId_whenChangePassword_thenReturnUser() throws Exception {
        String contentPassword = new ObjectMapper().writeValueAsString(passwords);
        user1.setPassword(passwords.get("oldPass"));
        mvc.perform(put(url + "/1/newPassword").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(contentPassword))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givingNonExistentId_whenChangePassword_thenReturnStatus404() throws Exception {
        String contentPassword = new ObjectMapper().writeValueAsString(passwords);
        user1.setPassword(passwords.get("oldPass"));
        mvc.perform(put(url + "/15/newPassword").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(contentPassword))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givingNonExistentOldPassword_whenChangePassword_thenReturnStatus409() throws Exception {
        user1.setPassword(passwords.get("oldPass"));
        passwords.replace("oldPass", "1234");
        String contentPassword = new ObjectMapper().writeValueAsString(passwords);
        mvc.perform(put(url + "/1/newPassword").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(contentPassword))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void whenGetUsers_thenReturnJsonArray() throws Exception {
        mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(users.size())))
                .andExpect(jsonPath("$[1].username", is(user2.getUsername())));
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenId_whenGetUser_thenReturnJson() throws Exception {
        mvc.perform(get(url + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user1.getUsername())));
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenNonExistentId_whenGetUser_thenReturnStatus404() throws Exception {
        mvc.perform(get(url + "/100").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenJsonUser_whenCreateUser_thenReturnStatus201() throws Exception {
        String contentUser = new ObjectMapper().writeValueAsString(user1);
        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(contentUser))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenJsonUserAndId_whenUpdateUser_thenReturnStatus200() throws Exception {
        String contentUser = new ObjectMapper().writeValueAsString(user2);
        mvc.perform(put(url + "/0").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(contentUser))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenJsonUserAndMismatchId_whenUpdateUser_thenReturnStatus409() throws Exception {
        String contentUser = new ObjectMapper().writeValueAsString(user2);
        mvc.perform(put(url + "/1").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(contentUser))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenId_whenDeleteUser_thenReturnStatus200() throws Exception {
        mvc.perform(delete(url + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenNonExistentId_whenDeleteUser_thenReturnStatus404() throws Exception {
        mvc.perform(delete(url + "/100").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenUserIdAndBookId_whenAddBook_thenReturnStatus200() throws Exception {
        mvc.perform(put(url + "/1/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenUserIdAndNonExistentBookId_whenAddBook_thenReturnStatus404() throws Exception {
        mvc.perform(put(url + "/1/100").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenUserIdAndAlreadyOwnedBook_whenAddBook_thenReturnStatus208() throws Exception {
        mvc.perform(put(url + "/1/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAlreadyReported());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenUserIdAndBookId_whenDeleteBook_thenReturnStatus200() throws Exception {
        mvc.perform(delete(url + "/1/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void givenUserIdAndNonExistentBookId_whenDeleteBook_thenReturnStatus200() throws Exception {
        mvc.perform(delete(url + "/1/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenNotAuthenticated_thenReturnStatus401() throws Exception {
        mvc.perform(get(url + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void whenGetCurrentUsername_thenReturnCurrentUser() throws Exception {
        mvc.perform(get(url + "/username").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user1.getUsername())))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.birthdate", is(user1.getBirthdate().toString())));
    }

    @WithMockUser(username = "gabriel", password = "123456")
    @Test
    public void whenGetUsersAndPagingSorting_thenReturnUsers() throws Exception {
        given(userRepository.findAllUsers(PageRequest.of(0,2, Sort.by("name")))).willReturn(users);

        mvc.perform(get(url + "?page=0&size=2&sort=name").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(users.size())))
                .andExpect(jsonPath("$[0].name", is(user1.getName())))
                .andExpect(jsonPath("$[1].name", is(user2.getName())));
    }

}
