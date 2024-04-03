package com.mjc.school.service.impl;

import com.mjc.school.repository.AuthorRepositoryInterface;
import com.mjc.school.repository.NewsRepositoryInterface;
import com.mjc.school.repository.TagRepositoryInterface;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.repository.utils.NewsParams;
import com.mjc.school.service.NewsServiceInterface;
import com.mjc.school.service.annotation.ValidatingNews;
import com.mjc.school.service.annotation.ValidatingNewsId;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.errorsexceptions.Errors;
import com.mjc.school.service.errorsexceptions.NotFoundException;
import com.mjc.school.service.mapper.AuthorMapper;
import com.mjc.school.service.mapper.CommentMapper;
import com.mjc.school.service.mapper.NewsMapper;
import com.mjc.school.service.mapper.TagMapper;
import com.mjc.school.service.utils.NewsParamsQuery;
import com.mjc.school.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class NewsService implements NewsServiceInterface {
    private final NewsRepositoryInterface newsRepository;
    private final AuthorRepositoryInterface authorRepository;
    private final TagRepositoryInterface tagRepository;
    private final NewsMapper mapper;
    private final AuthorMapper authorMapper;
    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;
    private final Validator validator;

    @Autowired
    public NewsService(NewsRepositoryInterface newsRepository,
                       AuthorRepositoryInterface authorRepository,
                       TagRepositoryInterface tagRepository,
                       NewsMapper mapper, AuthorMapper authorMapper,
                       TagMapper tagMapper, CommentMapper commentMapper,
                       Validator validator) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.authorMapper = authorMapper;
        this.tagMapper = tagMapper;
        this.commentMapper = commentMapper;
        this.validator = validator;
    }

    @Override
    public List<NewsDtoResponse> readAll(Integer pageNum, Integer pageSize, String sortBy) {

        if (sortBy.startsWith("id:") || sortBy.equals("id") ||
                sortBy.startsWith("title:") || sortBy.equals("title") ||
                sortBy.startsWith("content:") || sortBy.equals("content") ||
                sortBy.startsWith("authorid:") || sortBy.equals("authorid") ||
                sortBy.startsWith("createdate:") || sortBy.equals("createdate") ||
                sortBy.startsWith("lastupdatedate:") || sortBy.equals("lastupdatedate")) {
            return mapper.newsModelListToNewsDtoList(newsRepository.readAll(pageNum, pageSize, sortBy));
        } else {
            throw new NotFoundException(Errors.ERROR_REQUEST_PARAM_SORT.getErrorData(sortBy, true));
        }
    }

    @ValidatingNewsId
    @Override
    public NewsDtoResponse readById(Long id) {
        return newsRepository.readById(id)
                .map(mapper::newsModelToNewsDto)
                .orElseThrow(() -> new NotFoundException(Errors.ERROR_NEWS_ID_NOT_EXIST
                        .getErrorData(String.valueOf(id), true)));
    }

    @ValidatingNews
    @Transactional
    @Override
    public NewsDtoResponse create(NewsDtoRequest createRequest) {
        boolean authorExist = false;
        for (AuthorModel authorModel: authorRepository.getAllAuthors()) {
            if (Objects.equals(createRequest.getAuthorId(), authorModel.getId())) {
                authorExist = true;
                break;
            }
        }

        int tagsCounter = 0;
        if (createRequest.getTagIdList()!=null && !createRequest.getTagIdList().isEmpty()) {
            for (Long tagId: createRequest.getTagIdList()) {
                for (int i = 0; i < tagRepository.getAllTags().size(); i++) {
                    if (Objects.equals(tagRepository.getAllTags().get(i).getId(), tagId)) {
                        tagsCounter++;
                    }
                }
            }

            if (tagsCounter<createRequest.getTagIdList().size()) {
                throw new NotFoundException(Errors.ERROR_SOME_TAG_NOT_EXIST
                        .getErrorData("", false));
            }
        }

        if (authorExist) {
            NewsModel newsModel = newsRepository.create(mapper.newsDtoToNewsModel(createRequest));
            return mapper.newsModelToNewsDto(newsModel);
        } else {
            throw new NotFoundException(Errors.ERROR_AUTHOR_ID_NOT_EXIST
                    .getErrorData(String.valueOf(createRequest.getAuthorId()), true));
        }
    }

    @ValidatingNews
    @Transactional
    @Override
    public NewsDtoResponse update(NewsDtoRequest updateRequest) {
        validator.validateNewsId(updateRequest.getId());

        boolean equal = false;
        for (AuthorModel authorModel: authorRepository.getAllAuthors()) {
            if (Objects.equals(updateRequest.getAuthorId(), authorModel.getId())) {
                equal = true;
                break;
            }
        }

        int tagsCounter = 0;
        if (updateRequest.getTagIdList()!=null && !updateRequest.getTagIdList().isEmpty()) {
            for (Long tagId: updateRequest.getTagIdList()) {
                for (int i = 0; i < tagRepository.getAllTags().size(); i++) {
                    if (Objects.equals(tagRepository.getAllTags().get(i).getId(), tagId)) {
                        tagsCounter++;
                    }
                }
            }

            if (tagsCounter<updateRequest.getTagIdList().size()) {
                throw new NotFoundException(Errors.ERROR_SOME_TAG_NOT_EXIST
                        .getErrorData("", false));
            }
        }

        NewsDtoResponse newsDtoResponse = readById(updateRequest.getId());
        if (equal && newsDtoResponse!=null) {
            NewsModel newsModel = newsRepository.update(mapper.newsDtoToNewsModel(updateRequest));
            return mapper.newsModelToNewsDto(newsModel);
        } else if (!equal) {
            throw new NotFoundException(Errors.ERROR_AUTHOR_ID_NOT_EXIST
                        .getErrorData(String.valueOf(updateRequest.getAuthorId()), true));
        } else if (newsDtoResponse==null) {
            throw new NotFoundException(Errors.ERROR_NEWS_ID_NOT_EXIST
                        .getErrorData(String.valueOf(updateRequest.getId()), true));
        } else {
            throw new NotFoundException(Errors.ERROR_RESOURCE_NOT_FOUND.getErrorMessage());
        }
    }

    @ValidatingNewsId
    @Transactional
    @Override
    public boolean deleteById(Long id) {
        if (readById(id)!=null) {
            return newsRepository.deleteById(id);
        } else {
            return false;
        }
    }

    @Override
    public List<NewsDtoResponse> getAllNews() {
        return mapper.newsModelListToNewsDtoList(newsRepository.getAllNews());
    }

    @Transactional
    @Override
    public NewsDtoResponse partialUpdate(Long id, NewsDtoRequest newsDtoRequest) {
        validator.validateNewsId(id);
        Optional<NewsModel> existingNewsModel = newsRepository.readById(id);

        if (existingNewsModel.isPresent()) {
            if (newsDtoRequest.getTitle()!=null) {
                validator.validateNewsTitle(newsDtoRequest.getTitle());
                existingNewsModel.get().setTitle(newsDtoRequest.getTitle());
            }
            if (newsDtoRequest.getContent()!=null) {
                validator.validateNewsContent(newsDtoRequest.getContent());
                existingNewsModel.get().setContent(newsDtoRequest.getContent());
            }
            if (newsDtoRequest.getAuthorId()!=null) {
                validator.validateAuthorId(newsDtoRequest.getAuthorId());
                if (authorRepository.existById(newsDtoRequest.getAuthorId())) {
                    existingNewsModel.get().setAuthorModel(authorMapper.authorIdToAuthorModel(newsDtoRequest.getAuthorId()));
                } else {
                    throw new NotFoundException(Errors.ERROR_AUTHOR_ID_NOT_EXIST
                            .getErrorData(String.valueOf(newsDtoRequest.getAuthorId()), true));
                }
            }
            if (newsDtoRequest.getTagIdList()!=null) {
                for (Long tagId: newsDtoRequest.getTagIdList()) {
                    validator.validateTagId(tagId);
                    if (tagRepository.existById(tagId)) {
                        TagModel tagModel = tagRepository.readById(tagId).get();
                        existingNewsModel.get().getTagModelSet().add(tagModel);
                    } else {
                        throw new NotFoundException(Errors.ERROR_TAG_ID_NOT_EXIST
                                .getErrorData(String.valueOf(tagId), true));
                    }
                }
            }
            NewsModel newsModel = newsRepository.partialUpdate(id, existingNewsModel.get());
            return mapper.newsModelToNewsDto(newsModel);
        } else {
            throw new NotFoundException(Errors.ERROR_NEWS_ID_NOT_EXIST
                    .getErrorData(String.valueOf(id), true));
        }
    }

    @Override
    public List<NewsDtoResponse> getNewsByParams(NewsParamsQuery newsParamsQuery) {

        if (newsParamsQuery.getTagIds()!=null && !newsParamsQuery.getTagIds().isEmpty()) {
            for (Long tagId: newsParamsQuery.getTagIds()) {
                validator.validateTagId(tagId);
            }
        }
        if (newsParamsQuery.getTagNames()!=null && !newsParamsQuery.getTagNames().isEmpty()) {
            for (String tagName : newsParamsQuery.getTagNames()) {
                validator.validateTagName(tagName);
            }
        }
        if (newsParamsQuery.getAuthorName()!=null && newsParamsQuery.getAuthorName()!="") {
            validator.validateAuthorName(newsParamsQuery.getAuthorName());
        }
        if (newsParamsQuery.getTitle()!=null && newsParamsQuery.getTitle()!="") {
            validator.validateNewsTitle(newsParamsQuery.getTitle());
        }
        if (newsParamsQuery.getContent()!=null && newsParamsQuery.getContent()!="") {
            validator.validateNewsContent(newsParamsQuery.getContent());
        }
        if (newsParamsQuery.getSortField()!=null && newsParamsQuery.getSortField()!="") {
            newsParamsQuery.setSortField(newsParamsQuery.getSortField().toLowerCase());
            validator.validateSortField(newsParamsQuery.getSortField());
        }
        if (newsParamsQuery.getSortOrder()!=null && newsParamsQuery.getSortOrder()!="") {
            newsParamsQuery.setSortOrder(newsParamsQuery.getSortOrder().toLowerCase());
            validator.validateSortOrder(newsParamsQuery.getSortOrder());
        }

        NewsParams newsParams = new NewsParams(newsParamsQuery.getTagIds(), newsParamsQuery.getTagNames(),
                newsParamsQuery.getAuthorName(), newsParamsQuery.getTitle(), newsParamsQuery.getContent(),
                newsParamsQuery.getSortField(), newsParamsQuery.getSortOrder());
        List<NewsModel> newsModels = newsRepository.getNewsByParams(newsParams);

        if (newsModels!=null && !newsModels.isEmpty()) {
            return mapper.newsModelListToNewsDtoList(newsModels);
        } else {
            throw new NotFoundException(Errors.ERROR_NEWS_BY_PARAMS.getErrorData("",false));
        }
    }
}