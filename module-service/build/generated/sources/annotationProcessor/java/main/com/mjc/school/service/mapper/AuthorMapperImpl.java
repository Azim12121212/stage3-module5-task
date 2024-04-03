package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.dto.NewsDtoResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-04T01:49:39+0600",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.2.jar, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public AuthorModel authorDtoToAuthorModel(AuthorDtoRequest authorDto) {
        if ( authorDto == null ) {
            return null;
        }

        AuthorModel authorModel = new AuthorModel();

        authorModel.setId( authorDto.getId() );
        authorModel.setName( authorDto.getName() );

        return authorModel;
    }

    @Override
    public AuthorDtoResponse authorModelToAuthorDto(AuthorModel authorModel) {
        if ( authorModel == null ) {
            return null;
        }

        AuthorDtoResponse authorDtoResponse = new AuthorDtoResponse();

        authorDtoResponse.setNewsDtoResponseList( newsModelListToNewsDtoResponseList( authorModel.getNewsModelList() ) );
        authorDtoResponse.setId( authorModel.getId() );
        authorDtoResponse.setName( authorModel.getName() );
        authorDtoResponse.setCreateDate( authorModel.getCreateDate() );
        authorDtoResponse.setLastUpdateDate( authorModel.getLastUpdateDate() );

        return authorDtoResponse;
    }

    @Override
    public List<AuthorDtoResponse> authorModelListToAuthorDtoList(List<AuthorModel> authorModelList) {
        if ( authorModelList == null ) {
            return null;
        }

        List<AuthorDtoResponse> list = new ArrayList<AuthorDtoResponse>( authorModelList.size() );
        for ( AuthorModel authorModel : authorModelList ) {
            list.add( authorModelToAuthorDto( authorModel ) );
        }

        return list;
    }

    @Override
    public AuthorModel authorIdToAuthorModel(Long authorId) {
        if ( authorId == null ) {
            return null;
        }

        AuthorModel authorModel = new AuthorModel();

        authorModel.setId( authorId );

        return authorModel;
    }

    protected NewsDtoResponse newsModelToNewsDtoResponse(NewsModel newsModel) {
        if ( newsModel == null ) {
            return null;
        }

        NewsDtoResponse newsDtoResponse = new NewsDtoResponse();

        newsDtoResponse.setId( newsModel.getId() );
        newsDtoResponse.setTitle( newsModel.getTitle() );
        newsDtoResponse.setContent( newsModel.getContent() );
        newsDtoResponse.setCreateDate( newsModel.getCreateDate() );
        newsDtoResponse.setLastUpdateDate( newsModel.getLastUpdateDate() );

        return newsDtoResponse;
    }

    protected List<NewsDtoResponse> newsModelListToNewsDtoResponseList(List<NewsModel> list) {
        if ( list == null ) {
            return null;
        }

        List<NewsDtoResponse> list1 = new ArrayList<NewsDtoResponse>( list.size() );
        for ( NewsModel newsModel : list ) {
            list1.add( newsModelToNewsDtoResponse( newsModel ) );
        }

        return list1;
    }
}
