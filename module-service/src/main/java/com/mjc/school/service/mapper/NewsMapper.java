package com.mjc.school.service.mapper;

import com.mjc.school.repository.AuthorRepositoryInterface;
import com.mjc.school.repository.CommentRepositoryInterface;
import com.mjc.school.repository.TagRepositoryInterface;
import com.mjc.school.repository.model.*;
import com.mjc.school.service.dto.*;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@org.mapstruct.Mapper(componentModel = "spring", uses =
        {AuthorMapper.class, TagMapper.class, CommentMapper.class})
public abstract class NewsMapper {
    @Autowired
    protected AuthorRepositoryInterface authorRepository;
    @Autowired
    protected TagRepositoryInterface tagRepository;
    @Autowired
    protected CommentRepositoryInterface commentRepository;

    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "authorModel", expression = "java(authorRepository.readById(newsDto.getAuthorId()).get())")
    @Mapping(target = "tagModelSet", expression = "java(newsDto.getTagIdList()!=null ? tagModelListToTagModelSet(newsDto.getTagIdList().stream().map(tagId -> tagRepository.readById(tagId).get()).toList()) : null)")
    @Mapping(target = "commentModelList", ignore = true)
    public abstract NewsModel newsDtoToNewsModel(NewsDtoRequest newsDto);

    @Mapping(target = "authorDtoResponse", source = "authorModel")
    @Mapping(target = "tagDtoResponseSet", source = "tagModelSet")
    @Mapping(target = "commentDtoResponseList", source = "commentModelList")
    public abstract NewsDtoResponse newsModelToNewsDto(NewsModel newsModel);

    public abstract List<NewsDtoResponse> newsModelListToNewsDtoList(List<NewsModel> newsModelList);

    abstract Set<TagModel> tagModelListToTagModelSet(List<TagModel> tagModelList);
}