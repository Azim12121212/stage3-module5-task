package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import org.mapstruct.Mapping;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "newsModelList", ignore = true)
    AuthorModel authorDtoToAuthorModel(AuthorDtoRequest authorDto);

    @Mapping(target = "newsDtoResponseList", source = "newsModelList")
    AuthorDtoResponse authorModelToAuthorDto(AuthorModel authorModel);

    List<AuthorDtoResponse> authorModelListToAuthorDtoList(List<AuthorModel> authorModelList);

    @Mapping(target = "id", source = "authorId")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "newsModelList", ignore = true)
    AuthorModel authorIdToAuthorModel(Long authorId);
}