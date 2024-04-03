package com.mjc.school.service.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentParamsQuery {
    private String sortField;
    private String sortOrder;
}