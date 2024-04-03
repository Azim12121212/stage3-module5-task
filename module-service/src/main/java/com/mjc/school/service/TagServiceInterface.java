package com.mjc.school.service;

import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;

import java.util.List;
import java.util.Set;

public interface TagServiceInterface extends BaseService<TagDtoRequest, TagDtoResponse, Long> {

    TagDtoResponse partialUpdate(Long id, TagDtoRequest updateRequest);

    List<TagDtoResponse> getAllTags();

    TagDtoResponse readByName(String tagName);

    Set<TagDtoResponse> getTagsByNewsId(Long newsId);
}