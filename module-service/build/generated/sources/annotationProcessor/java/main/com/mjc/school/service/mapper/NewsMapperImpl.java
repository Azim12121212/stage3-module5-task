package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-04T01:49:39+0600",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.2.jar, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class NewsMapperImpl extends NewsMapper {

    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public NewsModel newsDtoToNewsModel(NewsDtoRequest newsDto) {
        if ( newsDto == null ) {
            return null;
        }

        NewsModel newsModel = new NewsModel();

        newsModel.setId( newsDto.getId() );
        newsModel.setTitle( newsDto.getTitle() );
        newsModel.setContent( newsDto.getContent() );

        newsModel.setAuthorModel( authorRepository.readById(newsDto.getAuthorId()).get() );
        newsModel.setTagModelSet( newsDto.getTagIdList()!=null ? tagModelListToTagModelSet(newsDto.getTagIdList().stream().map(tagId -> tagRepository.readById(tagId).get()).toList()) : null );

        return newsModel;
    }

    @Override
    public NewsDtoResponse newsModelToNewsDto(NewsModel newsModel) {
        if ( newsModel == null ) {
            return null;
        }

        NewsDtoResponse newsDtoResponse = new NewsDtoResponse();

        newsDtoResponse.setAuthorDtoResponse( authorMapper.authorModelToAuthorDto( newsModel.getAuthorModel() ) );
        newsDtoResponse.setTagDtoResponseSet( tagMapper.tagModelSetToTagDtoSet( newsModel.getTagModelSet() ) );
        newsDtoResponse.setCommentDtoResponseList( commentMapper.commentModelListToCommentDtoList( newsModel.getCommentModelList() ) );
        newsDtoResponse.setId( newsModel.getId() );
        newsDtoResponse.setTitle( newsModel.getTitle() );
        newsDtoResponse.setContent( newsModel.getContent() );
        newsDtoResponse.setCreateDate( newsModel.getCreateDate() );
        newsDtoResponse.setLastUpdateDate( newsModel.getLastUpdateDate() );

        return newsDtoResponse;
    }

    @Override
    public List<NewsDtoResponse> newsModelListToNewsDtoList(List<NewsModel> newsModelList) {
        if ( newsModelList == null ) {
            return null;
        }

        List<NewsDtoResponse> list = new ArrayList<NewsDtoResponse>( newsModelList.size() );
        for ( NewsModel newsModel : newsModelList ) {
            list.add( newsModelToNewsDto( newsModel ) );
        }

        return list;
    }

    @Override
    Set<TagModel> tagModelListToTagModelSet(List<TagModel> tagModelList) {
        if ( tagModelList == null ) {
            return null;
        }

        Set<TagModel> set = new HashSet<TagModel>( Math.max( (int) ( tagModelList.size() / .75f ) + 1, 16 ) );
        for ( TagModel tagModel : tagModelList ) {
            set.add( tagModel );
        }

        return set;
    }
}
