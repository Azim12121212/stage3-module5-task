package com.mjc.school.repository;

import com.mjc.school.repository.model.CommentModel;
import com.mjc.school.repository.utils.CommentParams;

import java.util.List;

public interface CommentRepositoryInterface extends BaseRepository<CommentModel, Long> {

    CommentModel partialUpdate(Long id, CommentModel entity);

    List<CommentModel> getCommentsByNewsId(Long newsId, CommentParams commentParams);
}