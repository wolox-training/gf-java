package com.wolox.training.repositories;

import com.wolox.training.exceptions.UserNotFoundException;
import com.wolox.training.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;
    private User testOtherUser;
    private List<User> testUsers;

    @Before
    public void setUp(){
        testUser = new User();
        testOtherUser = new User();

        testUser.setName("name123");
        testUser.setUsername("username123");
        testUser.setPassword("password123");
        testUser.setBirthdate(LocalDate.of(2000,01,26));
        testUser.setBooks(new ArrayList<>());

        testOtherUser.setName("name2");
        testOtherUser.setUsername("username2");
        testOtherUser.setPassword("password2");
        testOtherUser.setBirthdate(LocalDate.of(2001,01,26));
        testOtherUser.setBooks(new ArrayList<>());

        testUsers = new ArrayList<>();
        testUsers.add(testUser);
        testUsers.add(testOtherUser);
        entityManager.persist(testUser);
        entityManager.persist(testOtherUser);
        entityManager.flush();
    }

    @Test
    public void whenFindAll_thenReturnAllUsers(){
        List<User> repositoryList = userRepository.findAll();
        assertEquals(repositoryList.size(), testUsers.size());
        assertEquals(repositoryList.get(0).getUsername(), testUsers.get(0).getUsername());
        assertEquals(repositoryList.get(1).getBirthdate(), testUsers.get(1).getBirthdate());
    }


   @Test
    public void whenSaveUser_thenReturnUser(){
        User repositoryUser = userRepository.save(testUser);
        assertEquals(testUser.getUsername(), repositoryUser.getUsername());
    }

    @Test
    public void whenFindUser_thenReturnExpectedUser() throws UserNotFoundException {
        User repositoryUser = userRepository.findByUsername(testUser.getUsername()).orElseThrow(UserNotFoundException::new);
        assertEquals(testUser.getUsername(), repositoryUser.getUsername());
    }

    @Test(expected = UserNotFoundException.class)
    public void givenNotExistingId_whenFindUser_thenThrowException() throws UserNotFoundException {
        userRepository.findById(100L).orElseThrow(UserNotFoundException::new);
    }

    @Test
    public void whenDeleteUser_thenNotAppearInFindAll(){
        userRepository.delete(testUser);
        assertFalse(userRepository.findAll().contains(testUser));
        assertTrue(userRepository.findAll().contains(testOtherUser));
    }

    @Test
    public void whenGetByNameAndDatesBetween_thenReturnAList(){
        LocalDate date1 = LocalDate.of(1900,01,10);
        LocalDate date2 = LocalDate.of(2005,10,10);
        String nameContains = "name";
        List<User> testList = userRepository.findAllUsersByBirthdateBetweenAndNameContainsIgnoreCase(date1, date2, nameContains, null);
        assertFalse(testList.isEmpty());
        assertTrue(testList.contains(testUser));
    }

    @Test
    public void givingNonExistentData_whenGetByNameAndDatesBetween_thenReturnEmptyList(){
        LocalDate date1 = LocalDate.of(2000,01,10);
        LocalDate date2 = LocalDate.of(2005,10,10);
        String nameContains = "OOOOO";
        List<User> testList = userRepository.findAllUsersByBirthdateBetweenAndNameContainsIgnoreCase(date1, date2, nameContains, null);
        assertTrue(testList.isEmpty());

    }

    @Test
    public void whenGetByNameAndDatesBetweenQuery_thenReturnAList(){
        String nameContains = "me";
        List<User> testList = userRepository.findAllUsersByBirthdateBetweenAndNameContainsIgnoreCase(null, null, nameContains, null);
        assertFalse(testList.isEmpty());
        assertTrue(testList.contains(testUser));
    }

    @Test
    public void whenGetByNameAndDatesBetweenAndPagingSorting_thenReturnAList(){
        LocalDate date1 = LocalDate.of(1900,01,10);
        LocalDate date2 = LocalDate.of(2005,10,10);
        String nameContains = "e";
        List<User> testList = userRepository.findAllUsersByBirthdateBetweenAndNameContainsIgnoreCase(date1, date2, nameContains, PageRequest.of(0,2, Sort.by("name")));
        assertFalse(testList.isEmpty());
        assertEquals(testUser.getName(), testList.get(0).getName());
        assertEquals(testOtherUser.getName(), testList.get(1).getName());
    }
}
