package com.mjc.school.repository;

import com.mjc.school.repository.model.AuthorModel;

import java.util.List;
import java.util.Optional;

public interface AuthorRepositoryInterface extends BaseRepository<AuthorModel, Long> {

    List<AuthorModel> getAllAuthors();

    AuthorModel partialUpdate(Long id, AuthorModel entity);

    Optional<AuthorModel> readByName(String authorName);

    Optional<AuthorModel> getAuthorByNewsId(Long newsId);

    List<AuthorModel> getAuthorsByWrittenNews();
}