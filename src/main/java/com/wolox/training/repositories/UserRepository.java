package com.wolox.training.repositories;

import com.wolox.training.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);

    @Query("SELECT u FROM Users u " +
            "WHERE (cast(:date1 as date) is null OR u.birthdate >= :date1 " +
            "AND cast(:date2 as date) is null OR u.birthdate <= :date2) " +
            "AND (:name = '' OR UPPER(u.name) LIKE UPPER(concat('%', :name,'%')))")
    public List<User> findAllUsersByBirthdateBetweenAndNameContainsIgnoreCase(
            @Param("date1") LocalDate date1,
            @Param("date2") LocalDate date2,
            @Param("name") String name);

}
