package com.java.person.controller;

import com.java.person.dto.request.PersonRequest;
import com.java.person.dto.response.AppResponse;
import com.java.person.dto.response.PersonResponse;
import com.java.person.exception.PersonNotFoundException;
import com.java.person.handler.ResponseHandler;
import com.java.person.model.Person;
import com.java.person.service.PersonService;
import com.java.person.utils.GeneralUtil;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/person")
@Slf4j
public class PersonController {

    @Autowired
    private PersonService personService;

    @ApiResponses(value = {@ApiResponse(content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AppResponse.class)))})})
    @PostMapping()
    public ResponseEntity<Object> draft(@Valid @RequestBody PersonRequest requestBody, @RequestHeader("X-User-Email") String email) {
        log.info("Start save Development plan as draft: {}", GeneralUtil.ConvertObjectToString(requestBody));

        Person person = Person.builder()
                .firstName(requestBody.getFirstName())
                .lastName(requestBody.getLastName())
                .age(requestBody.getAge())
                .createdBy(email)
                .build();

        Person savedDraftDevelopmentPlan = personService.createDraft(person);
        Object responseBody = GeneralUtil.MapObjectToDto(savedDraftDevelopmentPlan);

        log.info("Success save Development plan: {}", GeneralUtil.ConvertObjectToString(responseBody));
        return ResponseHandler.generateResponse("Success", HttpStatus.CREATED, responseBody);
    }

    @ApiResponses(value = {@ApiResponse(content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))})})
    @PutMapping("/{uuid}")
    public ResponseEntity<Object> update(@PathVariable("uuid") String uuid, @Valid @RequestBody PersonRequest requestBody, @RequestHeader("X-User-Email") String username) throws PersonNotFoundException {

        log.info("Start updating Person data with ID {}: {}", uuid, GeneralUtil.ConvertObjectToString(requestBody));

        Person person = Person.builder()
                .firstName(requestBody.getFirstName())
                .lastName(requestBody.getLastName())
                .age(requestBody.getAge())
                .createdBy(username)
                .build();

        Person updatedPerson = personService.update(uuid, person, username);
        Object responseBody = GeneralUtil.MapObjectToDto(updatedPerson);

        log.info("Success updating Person data: {}", GeneralUtil.ConvertObjectToString(responseBody));
        return ResponseHandler.generateResponse("Success", HttpStatus.OK, responseBody);
    }

    @ApiResponses(value = {@ApiResponse(content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AppResponse.class)))})})
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Object> deleteDevelopmentPlan(@PathVariable("uuid") String uuid, @RequestHeader("X-User-Email") String username) throws PersonNotFoundException {
        log.info("Start delete Development Plan with id: {}", uuid);

        Person person = personService.delete(uuid, username);
        Object responseBody = GeneralUtil.MapObjectToDto(person);

        log.info("Success delete Development Plan at {}: {}", person.getCreatedDate(), GeneralUtil.ConvertObjectToString(responseBody));
        return ResponseHandler.generateResponse("Success", HttpStatus.OK, responseBody);
    }

    @ApiResponses(value = {@ApiResponse(content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AppResponse.class)))})})
    @GetMapping("/page")
    public ResponseEntity<Object> fetchPageable(@RequestParam(defaultValue = "0") int offset,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(required = false) String keyword,
                                                @RequestParam(defaultValue = "firstName") String sortBy,
                                                @RequestParam(defaultValue = "asc") String sort,
                                                @RequestParam(required = false) String fistName) {

        log.info("Start fetch development plan Pageable: {offset: {}, size: {}, keyword: {}}", offset, size, keyword);

        Page<PersonResponse> developmentPlans = personService.fetchPageable(offset, size, keyword, sortBy, sort, fistName);
        Page<Object> responseBody = developmentPlans.map(GeneralUtil::MapObjectToResponse);

        log.info("Success fetch development plan Pageable: {}", GeneralUtil.ConvertObjectToString(responseBody.getContent()));
        return ResponseHandler.generateResponse("Success", HttpStatus.OK, responseBody);
    }

    @ApiResponses(value = {@ApiResponse(content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AppResponse.class)))})})
    @GetMapping()
    public ResponseEntity<Object> fetchDevelopmentPlan() {

        log.info("Start fetch Sales Incentive");

        List<PersonResponse> developmentPlans = personService.fetchPersons();
        List<Object> responseBody = developmentPlans.stream().map(GeneralUtil::MapObjectToResponse).toList();

        log.info("Success fetch Sales Incentive: {}", GeneralUtil.ConvertObjectToString(responseBody));
        return ResponseHandler.generateResponse("Success", HttpStatus.OK, responseBody);
    }
}
