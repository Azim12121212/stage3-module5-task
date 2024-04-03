package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@org.mapstruct.Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "newsModelSet", ignore = true)
    TagModel tagDtoToTagModel(TagDtoRequest tagDto);

    @Mapping(target = "newsDtoResponseSet", source = "newsModelSet")
    TagDtoResponse tagModelToTagDto(TagModel tagModel);

    List<TagDtoResponse> tagModelListToTagDtoList(List<TagModel> tagModelList);

    Set<TagDtoResponse> tagModelSetToTagDtoSet(Set<TagModel> tagModelSet);
}