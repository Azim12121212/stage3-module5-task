package com.mjc.school.controller.interfaces;

import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthorControllerInterface extends BaseController<AuthorDtoRequest, AuthorDtoResponse, Long> {

    ResponseEntity<AuthorDtoResponse> partialUpdate(Long id, AuthorDtoRequest updateRequest);

    ResponseEntity<AuthorDtoResponse> readByName(String authorName);

    ResponseEntity<AuthorDtoResponse> getAuthorByNewsId(Long newsId);

    ResponseEntity<List<AuthorDtoResponse>> getAuthorsByWrittenNews();
}