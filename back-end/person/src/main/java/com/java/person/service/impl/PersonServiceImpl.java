package com.java.person.service.impl;

import com.java.person.dto.response.PersonResponse;
import com.java.person.exception.PersonNotFoundException;
import com.java.person.model.Person;
import com.java.person.repository.PersonRepository;
import com.java.person.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public Person createDraft(Person person) {
        try {
            person.setUuid(UUID.randomUUID().toString());
            person.setCreatedDate(new Date());
            person.setDeleted(false);

            personRepository.save(person);

        } catch (Exception e) {
            log.error("Terjadi kesalahan saat menyimpan Draft Sales Incentive: {}", e.getMessage());
            throw new RuntimeException("Gagal menyimpan Draft Sales Incentive, silakan coba lagi.");
        }

        return person;
    }

    @Override
    public Person update(String uuid, Person person, String username) throws PersonNotFoundException {
        try {
            Optional<Person> existingPersonOpt = personRepository.findByUuid(uuid);

            if (existingPersonOpt.isEmpty()) {
                throw new PersonNotFoundException("Person dengan UUID " + uuid + " tidak ditemukan.");
            }

            Person existingPerson = existingPersonOpt.get();
            existingPerson.setFirstName(person.getFirstName());
            existingPerson.setLastName(person.getLastName());
            existingPerson.setAge(person.getAge());
            existingPerson.setUpdatedDate(new Date());
            existingPerson.setDeleted(false);

            personRepository.save(existingPerson);
            return existingPerson;

        } catch (Exception e) {
            log.error("Terjadi kesalahan saat memperbarui Person: {}", e.getMessage());
            throw new RuntimeException("Gagal memperbarui Person, silakan coba lagi.");
        }
    }


    @Override
    public Person delete(String uuid, String username) throws PersonNotFoundException {
        try {
            Optional<Person> existingPersonOpt = personRepository.findByUuid(uuid);

            if (existingPersonOpt.isEmpty()) {
                throw new PersonNotFoundException("Person dengan UUID " + uuid + " tidak ditemukan.");
            }

            Person existingPerson = existingPersonOpt.get();
            existingPerson.setDeleted(true);
            existingPerson.setDeletedBy(username);
            existingPerson.setDeletedDate(new Date());
            existingPerson.setUpdatedBy(username);

            personRepository.save(existingPerson);
            return existingPerson;

        } catch (Exception e) {
            log.error("Terjadi kesalahan saat menghapus Person: {}", e.getMessage());
            throw new RuntimeException("Gagal menghapus Person, silakan coba lagi.");
        }
    }

    @Override
    public List<PersonResponse> fetchPersons() {
        List<Person> persons = personRepository.findByIsDeleted(false);

        return persons.stream().map(person -> {
            PersonResponse response = new PersonResponse();
            response.setUuid(person.getUuid());
            response.setFirstName(person.getFirstName());
            response.setLastName(person.getLastName());
            response.setAge(person.getAge());
            response.setCreateDate(person.getCreatedDate());
            response.setCreateBy(person.getCreatedBy());
            response.setUpdatedBy(person.getUpdatedBy());
            response.setDeletedBy(person.getDeletedBy());
            return response;
        }).collect(Collectors.toList());
    }

    public Page<PersonResponse> fetchPageable(int offset, int size, String keyword, String sortBy, String direction, String firstName) {
        Pageable pageable = PageRequest.of(offset, size);

        List<PersonResponse> persons = fetchPersons();
        List<PersonResponse> filteredPersons = new ArrayList<>();

        if (StringUtils.isEmpty(keyword)) {
            filteredPersons.addAll(persons);
        } else {
            keyword = keyword.toLowerCase();
            for (PersonResponse person : persons) {
                if (person.getFirstName().toLowerCase().contains(keyword) ||
                        person.getLastName().toLowerCase().contains(keyword)) {
                    filteredPersons.add(person);
                }
            }
        }

        // Sorting logic
        if (sortBy != null && direction != null) {
            Comparator<PersonResponse> comparator = getComparator(sortBy);
            if ("desc".equalsIgnoreCase(direction)) {
                comparator = comparator.reversed();
            }
            filteredPersons = filteredPersons.stream().sorted(comparator).collect(Collectors.toList());
        }

        int start = Math.min(offset * size, filteredPersons.size());
        int end = Math.min(start + size, filteredPersons.size());
        List<PersonResponse> pageContent = filteredPersons.subList(start, end);

        return new PageImpl<>(pageContent, pageable, filteredPersons.size());
    }

    private Comparator<PersonResponse> getComparator(String sortBy) {
        switch (sortBy) {
            case "firstName": return Comparator.comparing(PersonResponse::getFirstName);
            case "lastName": return Comparator.comparing(PersonResponse::getLastName);
            case "age": return Comparator.comparingInt(PersonResponse::getAge);
            default: return Comparator.comparing(PersonResponse::getFirstName);
        }
    }
}
