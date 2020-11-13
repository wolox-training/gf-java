package com.wolox.training.repositories;

import com.wolox.training.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);
    public List<User> findAllUsersByBirthdateBetweenAndNameContainsIgnoreCase(LocalDate date1, LocalDate date2, String name);
}
