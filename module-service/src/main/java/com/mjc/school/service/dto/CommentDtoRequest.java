package com.mjc.school.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDtoRequest {
    private Long id;
    private String content;
    private Long newsId;

    public CommentDtoRequest(String content, Long newsId) {
        this.content = content;
        this.newsId = newsId;
    }
}