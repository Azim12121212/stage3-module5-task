package com.mjc.school.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthorDtoRequest {
    private Long id;
    private String name;

    public AuthorDtoRequest(String name) {
        this.name = name;
    }
}