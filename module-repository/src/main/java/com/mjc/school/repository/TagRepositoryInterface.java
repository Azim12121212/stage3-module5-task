package com.mjc.school.repository;

import com.mjc.school.repository.model.TagModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepositoryInterface extends BaseRepository<TagModel, Long> {

    TagModel partialUpdate(Long id, TagModel entity);

    List<TagModel> getAllTags();

    Optional<TagModel> readByName(String tagName);

    Set<TagModel> getTagsByNewsId(Long newsId);
}