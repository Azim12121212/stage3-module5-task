package com.mjc.school.service;

import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.utils.CommentParamsQuery;

import java.util.List;

public interface CommentServiceInterface extends BaseService<CommentDtoRequest, CommentDtoResponse, Long> {

    CommentDtoResponse partialUpdate(Long id, CommentDtoRequest updateRequest);

    List<CommentDtoResponse> getCommentsByNewsId(Long newsId, CommentParamsQuery commentParamsQuery);
}