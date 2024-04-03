package com.mjc.school.repository.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentParams {
    private String sortField;
    private String sortOrder;
}