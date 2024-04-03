package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-04T01:49:39+0600",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.2.jar, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class TagMapperImpl implements TagMapper {

    @Override
    public TagModel tagDtoToTagModel(TagDtoRequest tagDto) {
        if ( tagDto == null ) {
            return null;
        }

        TagModel tagModel = new TagModel();

        tagModel.setId( tagDto.getId() );
        tagModel.setName( tagDto.getName() );

        return tagModel;
    }

    @Override
    public TagDtoResponse tagModelToTagDto(TagModel tagModel) {
        if ( tagModel == null ) {
            return null;
        }

        TagDtoResponse tagDtoResponse = new TagDtoResponse();

        tagDtoResponse.setNewsDtoResponseSet( newsModelSetToNewsDtoResponseSet( tagModel.getNewsModelSet() ) );
        tagDtoResponse.setId( tagModel.getId() );
        tagDtoResponse.setName( tagModel.getName() );

        return tagDtoResponse;
    }

    @Override
    public List<TagDtoResponse> tagModelListToTagDtoList(List<TagModel> tagModelList) {
        if ( tagModelList == null ) {
            return null;
        }

        List<TagDtoResponse> list = new ArrayList<TagDtoResponse>( tagModelList.size() );
        for ( TagModel tagModel : tagModelList ) {
            list.add( tagModelToTagDto( tagModel ) );
        }

        return list;
    }

    @Override
    public Set<TagDtoResponse> tagModelSetToTagDtoSet(Set<TagModel> tagModelSet) {
        if ( tagModelSet == null ) {
            return null;
        }

        Set<TagDtoResponse> set = new HashSet<TagDtoResponse>( Math.max( (int) ( tagModelSet.size() / .75f ) + 1, 16 ) );
        for ( TagModel tagModel : tagModelSet ) {
            set.add( tagModelToTagDto( tagModel ) );
        }

        return set;
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

    protected Set<NewsDtoResponse> newsModelSetToNewsDtoResponseSet(Set<NewsModel> set) {
        if ( set == null ) {
            return null;
        }

        Set<NewsDtoResponse> set1 = new HashSet<NewsDtoResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( NewsModel newsModel : set ) {
            set1.add( newsModelToNewsDtoResponse( newsModel ) );
        }

        return set1;
    }
}
