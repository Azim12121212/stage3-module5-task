package com.mjc.school.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewsDtoRequest {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private List<Long> tagIdList;

    public NewsDtoRequest(String title, String content, Long authorId) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }

    public NewsDtoRequest(String title, String content, Long authorId, List<Long> tagIdList) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.tagIdList = tagIdList;
    }

    public NewsDtoRequest(Long id, String title, String content, Long authorId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }
}