package com.mjc.school.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class TagDtoResponse extends RepresentationModel<AuthorDtoResponse> {
    private Long id;
    private String name;
    @JsonIgnoreProperties(value = {"authorDtoResponse", "tagDtoResponseSet", "commentDtoResponseList"})
    private Set<NewsDtoResponse> newsDtoResponseSet;

    public TagDtoResponse(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TagDtoResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}