package com.java.person.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonResponse {

    private String uuid;
    private String firstName;
    private String lastName;
    private Integer age;
    private Date createDate;
    private String createBy;
    private String updatedBy;
    private String deletedBy;

}
