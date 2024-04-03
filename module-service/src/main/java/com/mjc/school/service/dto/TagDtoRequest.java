package com.mjc.school.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagDtoRequest {
    private Long id;
    private String name;

    public TagDtoRequest(String name) {
        this.name = name;
    }
}