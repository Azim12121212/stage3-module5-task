package com.mjc.school.controller.interfaces;

import com.mjc.school.controller.interfaces.BaseController;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface TagControllerInterface extends BaseController<TagDtoRequest, TagDtoResponse, Long> {

    ResponseEntity<TagDtoResponse> partialUpdate(Long id, TagDtoRequest updateRequest);

    ResponseEntity<TagDtoResponse> readByName(String tagName);

    ResponseEntity<Set<TagDtoResponse>> getTagsByNewsId(Long newsId);
}