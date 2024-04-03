package com.mjc.school.service.impl;

import com.mjc.school.repository.CommentRepositoryInterface;
import com.mjc.school.repository.NewsRepositoryInterface;
import com.mjc.school.repository.model.CommentModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.utils.CommentParams;
import com.mjc.school.service.CommentServiceInterface;
import com.mjc.school.service.annotation.ValidatingComment;
import com.mjc.school.service.annotation.ValidatingCommentId;
import com.mjc.school.service.annotation.ValidatingNewsId;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.errorsexceptions.Errors;
import com.mjc.school.service.errorsexceptions.NotFoundException;
import com.mjc.school.service.mapper.CommentMapper;
import com.mjc.school.service.utils.CommentParamsQuery;
import com.mjc.school.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentService implements CommentServiceInterface {
    private final CommentRepositoryInterface commentRepository;
    private final NewsRepositoryInterface newsRepository;
    private final CommentMapper mapper;
    private final Validator validator;

    @Autowired
    public CommentService(CommentRepositoryInterface commentRepository,
                          NewsRepositoryInterface newsRepository,
                          CommentMapper mapper, Validator validator) {
        this.commentRepository = commentRepository;
        this.newsRepository = newsRepository;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public List<CommentDtoResponse> readAll(Integer pageNum, Integer pageSize, String sortBy) {

        if (sortBy.equals("id") || sortBy.startsWith("id:") ||
                sortBy.equals("content") || sortBy.startsWith("content:") ||
                sortBy.equals("newsid") || sortBy.startsWith("newsid:") ||
                sortBy.equals("createdate") || sortBy.startsWith("createdate:") ||
                sortBy.equals("lastupdatedate") || sortBy.startsWith("lastupdatedate:")) {
            return mapper.commentModelListToCommentDtoList(commentRepository.readAll(pageNum, pageSize, sortBy));
        } else {
            throw new NotFoundException(Errors.ERROR_REQUEST_PARAM_SORT.getErrorData(sortBy, true));
        }
    }

    @ValidatingCommentId
    @Override
    public CommentDtoResponse readById(Long id) {
        return commentRepository.readById(id)
                .map(mapper::commentModelToCommentDto)
                .orElseThrow(() -> new NotFoundException(Errors.ERROR_COMMENT_ID_NOT_EXIST
                        .getErrorData(String.valueOf(id), true)));
    }

    @ValidatingComment
    @Transactional
    @Override
    public CommentDtoResponse create(CommentDtoRequest createRequest) {
        boolean equal = false;
        for (NewsModel newsModel: newsRepository.getAllNews()) {
            if (Objects.equals(createRequest.getNewsId(), newsModel.getId())) {
                equal = true;
                break;
            }
        }
        if (equal) {
            CommentModel commentModel = commentRepository.create(mapper.commentDtoToCommentModel(createRequest));
            return mapper.commentModelToCommentDto(commentModel);
        } else {
            throw new NotFoundException(Errors.ERROR_NEWS_ID_NOT_EXIST
                    .getErrorData(String.valueOf(createRequest.getNewsId()), true));
        }
    }

    @ValidatingComment
    @Transactional
    @Override
    public CommentDtoResponse update(CommentDtoRequest updateRequest) {
        validator.validateCommentId(updateRequest.getId());

        boolean equal = false;
        for (NewsModel newsModel: newsRepository.getAllNews()) {
            if (Objects.equals(updateRequest.getNewsId(), newsModel.getId())) {
                equal = true;
                break;
            }
        }
        CommentDtoResponse commentDtoResponse = readById(updateRequest.getId());
        if (equal && commentDtoResponse!=null) {
            CommentModel commentModel = commentRepository.update(mapper.commentDtoToCommentModel(updateRequest));
            return mapper.commentModelToCommentDto(commentModel);
        } else if (!equal) {
            throw new NotFoundException(Errors.ERROR_NEWS_ID_NOT_EXIST
                    .getErrorData(String.valueOf(updateRequest.getNewsId()), true));
        } else if (commentDtoResponse == null) {
            throw new NotFoundException(Errors.ERROR_COMMENT_ID_NOT_EXIST
                    .getErrorData(String.valueOf(updateRequest.getId()), true));
        } else {
            throw new NotFoundException(Errors.ERROR_RESOURCE_NOT_FOUND.getErrorMessage());
        }
    }

    @ValidatingCommentId
    @Transactional
    @Override
    public boolean deleteById(Long id) {
        if (readById(id)!=null) {
            return commentRepository.deleteById(id);
        } else {
            return false;
        }
    }

    @Transactional
    @Override
    public CommentDtoResponse partialUpdate(Long id, CommentDtoRequest updateRequest) {
        validator.validateCommentId(id);
        Optional<CommentModel> existingCommentModel = commentRepository.readById(id);

        if (existingCommentModel.isPresent()) {
            if (updateRequest.getContent()!=null) {
                validator.validateCommentContent(updateRequest.getContent());
                existingCommentModel.get().setContent(updateRequest.getContent());
            }
            if (updateRequest.getNewsId()!=null) {
                validator.validateNewsId(updateRequest.getNewsId());
                if (newsRepository.existById(updateRequest.getNewsId())) {
                    existingCommentModel.get().setNewsModel(mapper.newsIdToNewsModel(updateRequest.getNewsId()));
                } else {
                    throw new NotFoundException(Errors.ERROR_NEWS_ID_NOT_EXIST
                            .getErrorData(String.valueOf(updateRequest.getNewsId()), true));
                }
            }
            CommentModel commentModel = commentRepository.partialUpdate(id, existingCommentModel.get());
            return mapper.commentModelToCommentDto(commentModel);
        } else {
            throw new NotFoundException(Errors.ERROR_COMMENT_ID_NOT_EXIST
                    .getErrorData(String.valueOf(id), true));
        }
    }

    // Get Comments by news id – return comments by provided news id.
    @ValidatingNewsId
    @Override
    public List<CommentDtoResponse> getCommentsByNewsId(Long newsId, CommentParamsQuery commentParamsQuery) {

        if (newsRepository.readById(newsId).isPresent()) {

            if (commentParamsQuery.getSortField()!=null && !commentParamsQuery.getSortField().isEmpty()) {
                commentParamsQuery.setSortField(commentParamsQuery.getSortField().toLowerCase());
                validator.validateSortField(commentParamsQuery.getSortField());
            }
            if (commentParamsQuery.getSortOrder()!=null && !commentParamsQuery.getSortOrder().isEmpty()) {
                commentParamsQuery.setSortOrder(commentParamsQuery.getSortOrder().toLowerCase());
                validator.validateSortOrder(commentParamsQuery.getSortOrder());
            }

            CommentParams commentParams = new CommentParams(
                    commentParamsQuery.getSortField(),
                    commentParamsQuery.getSortOrder());

            List<CommentModel> commentModels = commentRepository.getCommentsByNewsId(newsId, commentParams);

            if (commentModels!=null && !commentModels.isEmpty()) {
                return mapper.commentModelListToCommentDtoList(commentModels);
            } else {
                throw new NotFoundException(Errors.ERROR_COMMENT_BY_NEWS_ID
                        .getErrorData(String.valueOf(newsId), true));
            }
        } else {
            throw new NotFoundException(Errors.ERROR_NEWS_ID_NOT_EXIST
                    .getErrorData(String.valueOf(newsId), true));
        }
    }
}