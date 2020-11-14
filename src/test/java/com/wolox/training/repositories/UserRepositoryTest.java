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
        testUser = new User("Gaby26","123456", "Gabriel Fernandez", LocalDate.of(2000, 01, 26));
        testOtherUser = new User("TestUsername","123456", "TestName", LocalDate.of(2005, 10, 15));
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

    @Test(expected = ConstraintViolationException.class)
    public void whenCreateUserWithoutUsername_thenThrowException(){
        User user = new User(null, "Gabriel Fernandez", LocalDate.of(2000, 01, 26));
        userRepository.save(user);
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
        String nameContains = "Gabriel Fernandez";
        List<User> testList = userRepository.findAllUsersByBirthdateBetweenAndNameContainsIgnoreCase(date1, date2, nameContains);
        assertFalse(testList.isEmpty());
        assertTrue(testList.contains(testUser));
    }

    @Test
    public void givingNonExistentData_whenGetByNameAndDatesBetween_thenReturnEmptyList(){
        LocalDate date1 = LocalDate.of(2000,01,10);
        LocalDate date2 = LocalDate.of(2005,10,10);
        String nameContains = "OOOOO";
        List<User> testList = userRepository.findAllUsersByBirthdateBetweenAndNameContainsIgnoreCase(date1, date2, nameContains);
        assertTrue(testList.isEmpty());

    }

    @Test
    public void whenGetByNameAndDatesBetweenQuery_thenReturnAList(){
        String nameContains = "el";
        List<User> testList = userRepository.findAllUsersByBirthdateBetweenAndNameContainsIgnoreCase(null, null, nameContains);
        assertFalse(testList.isEmpty());
        assertTrue(testList.contains(testUser));
    }
}
