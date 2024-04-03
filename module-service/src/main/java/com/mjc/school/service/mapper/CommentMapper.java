package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.CommentModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import org.mapstruct.Mapping;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "newsModel", source = "newsId")
    CommentModel commentDtoToCommentModel(CommentDtoRequest commentDto);

    @Mapping(target = "newsDtoResponse", source = "newsModel")
    CommentDtoResponse commentModelToCommentDto(CommentModel commentModel);

    List<CommentDtoResponse> commentModelListToCommentDtoList(List<CommentModel> commentModelList);

    @Mapping(target = "id", source = "commentId")
    @Mapping(target = "content", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "newsModel", ignore = true)
    CommentModel commentIdToCommentModel(Long commentId);

    @Mapping(target = "id", source = "newsId")
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "content", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "authorModel", ignore = true)
    @Mapping(target = "tagModelSet", ignore = true)
    @Mapping(target = "commentModelList", ignore = true)
    NewsModel newsIdToNewsModel(Long newsId);
}