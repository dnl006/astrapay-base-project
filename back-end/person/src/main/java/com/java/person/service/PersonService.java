package com.java.person.service;

import com.java.person.dto.response.PersonResponse;
import com.java.person.exception.PersonNotFoundException;
import com.java.person.model.Person;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PersonService {

    public Person createDraft(Person person);

    public Person update(String uuid, Person person, String username) throws PersonNotFoundException;

    public Person delete(String uuid, String username) throws PersonNotFoundException;

    public List<PersonResponse> fetchPersons();

    public Page<PersonResponse> fetchPageable(int offset, int size, String keyword, String sortBy, String direction, String firstName);

}
