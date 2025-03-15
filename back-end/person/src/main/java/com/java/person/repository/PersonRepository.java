package com.java.person.repository;

import com.java.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUuid(String uuid);

    List<Person> findByIsDeleted(boolean isDeleted);
}
