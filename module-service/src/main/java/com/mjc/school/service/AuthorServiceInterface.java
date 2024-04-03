package com.mjc.school.service;

import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;

import java.util.List;

public interface AuthorServiceInterface extends BaseService<AuthorDtoRequest, AuthorDtoResponse, Long> {

    List<AuthorDtoResponse> getAllAuthors();

    AuthorDtoResponse partialUpdate(Long id, AuthorDtoRequest updateRequest);

    AuthorDtoResponse readByName(String authorName);

    AuthorDtoResponse getAuthorByNewsId(Long newsId);

    List<AuthorDtoResponse> getAuthorsByWrittenNews();
}