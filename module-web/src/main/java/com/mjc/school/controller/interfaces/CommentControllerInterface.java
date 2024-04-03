package com.mjc.school.controller.interfaces;

import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.utils.CommentParamsQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentControllerInterface extends BaseController<CommentDtoRequest, CommentDtoResponse, Long> {

    ResponseEntity<CommentDtoResponse> partialUpdate(Long id, CommentDtoRequest updateRequest);
    ResponseEntity<List<CommentDtoResponse>> getCommentsByNewsId(Long newsId, CommentParamsQuery commentParamsQuery);
}