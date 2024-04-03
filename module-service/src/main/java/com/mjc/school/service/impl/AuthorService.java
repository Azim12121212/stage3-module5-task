package com.mjc.school.service.impl;

import com.mjc.school.repository.AuthorRepositoryInterface;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.service.AuthorServiceInterface;
import com.mjc.school.service.annotation.ValidatingAuthor;
import com.mjc.school.service.annotation.ValidatingAuthorId;
import com.mjc.school.service.annotation.ValidatingNewsId;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.errorsexceptions.Errors;
import com.mjc.school.service.errorsexceptions.NotFoundException;
import com.mjc.school.service.errorsexceptions.ValidatorException;
import com.mjc.school.service.mapper.AuthorMapper;
import com.mjc.school.service.mapper.NewsMapper;
import com.mjc.school.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService implements AuthorServiceInterface {
    private final AuthorRepositoryInterface authorRepository;
    private final AuthorMapper mapper;
    private final NewsMapper newsMapper;
    private final Validator validator;

    @Autowired
    public AuthorService(AuthorRepositoryInterface authorRepository,
                         AuthorMapper mapper, NewsMapper newsMapper, Validator validator) {
        this.authorRepository = authorRepository;
        this.mapper = mapper;
        this.newsMapper = newsMapper;
        this.validator = validator;
    }

    @Override
    public List<AuthorDtoResponse> readAll(Integer pageNum, Integer pageSize, String sortBy) {

        if (sortBy.equals("id") || sortBy.startsWith("id:") ||
                sortBy.equals("name") || sortBy.startsWith("name:") ||
                sortBy.equals("createdate") || sortBy.startsWith("createdate:") ||
                sortBy.equals("lastupdatedate") || sortBy.startsWith("lastupdatedate:")) {
            return mapper.authorModelListToAuthorDtoList(authorRepository.readAll(pageNum, pageSize, sortBy));
        } else {
            throw new NotFoundException(Errors.ERROR_REQUEST_PARAM_SORT.getErrorData(sortBy, true));
        }
    }

    @ValidatingAuthorId
    @Override
    public AuthorDtoResponse readById(Long id) {
        return authorRepository.readById(id)
                .map(mapper::authorModelToAuthorDto)
                .orElseThrow(() -> new NotFoundException(Errors.ERROR_AUTHOR_ID_NOT_EXIST.getErrorData(String.valueOf(id), true)));
    }

    @ValidatingAuthor
    @Transactional
    @Override
    public AuthorDtoResponse create(AuthorDtoRequest createRequest) {
        AuthorModel authorModel = authorRepository.create(mapper.authorDtoToAuthorModel(createRequest));
        return mapper.authorModelToAuthorDto(authorModel);
    }

    @ValidatingAuthor
    @Transactional
    @Override
    public AuthorDtoResponse update(AuthorDtoRequest updateRequest) {
        validator.validateAuthorId(updateRequest.getId());

        if (readById(updateRequest.getId())!=null) {
            AuthorModel authorModel = authorRepository.update(mapper.authorDtoToAuthorModel(updateRequest));
            return mapper.authorModelToAuthorDto(authorModel);
        } else {
            throw new NotFoundException(Errors.ERROR_AUTHOR_ID_NOT_EXIST
                    .getErrorData(String.valueOf(updateRequest.getId()), true));
        }
    }

    @ValidatingAuthorId
    @Transactional
    @Override
    public boolean deleteById(Long id) {
        if (readById(id)!=null) {
            return authorRepository.deleteById(id);
        } else {
            return false;
        }
    }

    @Override
    public List<AuthorDtoResponse> getAllAuthors() {
        return mapper.authorModelListToAuthorDtoList(authorRepository.getAllAuthors());
    }

    @Transactional
    @Override
    public AuthorDtoResponse partialUpdate(Long id, AuthorDtoRequest updateRequest) {
        validator.validateAuthorId(id);
        Optional<AuthorModel> existingAuthorModel = authorRepository.readById(id);

        if (existingAuthorModel.isPresent()) {
            if (updateRequest.getName()!=null) {
                validator.validateAuthorName(updateRequest.getName());
                existingAuthorModel.get().setName(updateRequest.getName());
            }
            AuthorModel authorModel = authorRepository.partialUpdate(id, existingAuthorModel.get());
            return mapper.authorModelToAuthorDto(authorModel);
        } else {
            throw new NotFoundException(Errors.ERROR_AUTHOR_ID_NOT_EXIST.getErrorData(String.valueOf(id), true));
        }
    }

    // Get Author by part name
    @Override
    public AuthorDtoResponse readByName(String authorName) {
        validator.validateAuthorName(authorName);
        return authorRepository.readByName(authorName)
                .map(mapper::authorModelToAuthorDto)
                .orElseThrow(() -> new NotFoundException(Errors.ERROR_AUTHOR_PART_NAME_NOT_EXIST
                        .getErrorData(authorName, true)));
    }

    // Get Author by news id – return author by provided news id.
    @ValidatingNewsId
    @Override
    public AuthorDtoResponse getAuthorByNewsId(Long newsId) {
        Optional<AuthorModel> authorModel = authorRepository.getAuthorByNewsId(newsId);
        if (authorModel.isPresent()) {
            return mapper.authorModelToAuthorDto(authorModel.get());
        } else {
            throw new NotFoundException(Errors.ERROR_AUTHOR_NEWS_ID_NOT_EXIST.getErrorData(String.valueOf(newsId), true));
        }
    }

    // Get Authors with amount of written news. Sort by news amount Desc.
    @Override
    public List<AuthorDtoResponse> getAuthorsByWrittenNews() {
        List<AuthorModel> authorModels = authorRepository.getAuthorsByWrittenNews();
        if (authorModels!=null) {
            return mapper.authorModelListToAuthorDtoList(authorModels);
        } else {
            throw new NotFoundException(Errors.ERROR_AUTHORS_NOT_EXIST.getErrorData("", false));
        }
    }
}