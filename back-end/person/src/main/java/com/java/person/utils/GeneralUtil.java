package com.java.person.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.person.dto.response.PersonResponse;
import com.java.person.model.Person;

import java.util.Map;
import java.util.stream.Collectors;

public class GeneralUtil {

    public static String ConvertObjectToString(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException: Failed to convert Object to String");
        }
    }

    public static String ConvertMapAsString(Map<String, Object> map) {
        return map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    public static Object MapObjectToDto(Object object) {
        if (object instanceof Person) {
            Person person = (Person) object;
            return PersonResponse.builder()
                    .uuid(person.getUuid())
                    .firstName(person.getFirstName())
                    .lastName(person.getLastName())
                    .age(person.getAge())
                    .createDate(person.getCreatedDate())
                    .createBy(person.getCreatedBy())
                    .deletedBy(person.getDeletedBy())
                    .updatedBy(person.getUpdatedBy())
                    .build();
        }
        throw new RuntimeException("Failed to convert object");
    }

    public static Object MapObjectToResponse(Object object) {
        if (object instanceof PersonResponse personResponse){
            return PersonResponse.builder()
                    .uuid(personResponse.getUuid())
                    .firstName(personResponse.getFirstName())
                    .lastName(personResponse.getLastName())
                    .age(personResponse.getAge())
                    .createBy(personResponse.getCreateBy())
                    .deletedBy(personResponse.getDeletedBy())
                    .updatedBy(personResponse.getUpdatedBy())
                    .build();
        }

        throw new RuntimeException("Failed to convert object");
    }
}
