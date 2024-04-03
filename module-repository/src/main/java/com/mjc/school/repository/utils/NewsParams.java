package com.mjc.school.repository.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewsParams {
    private List<Long> tagIds;
    private List<String> tagNames;
    private String authorName;
    private String title;
    private String content;
    private String sortField;
    private String sortOrder;
}