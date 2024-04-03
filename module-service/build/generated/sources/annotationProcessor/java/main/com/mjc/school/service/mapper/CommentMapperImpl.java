package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.CommentModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
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
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentModel commentDtoToCommentModel(CommentDtoRequest commentDto) {
        if ( commentDto == null ) {
            return null;
        }

        CommentModel commentModel = new CommentModel();

        commentModel.setNewsModel( newsIdToNewsModel( commentDto.getNewsId() ) );
        commentModel.setId( commentDto.getId() );
        commentModel.setContent( commentDto.getContent() );

        return commentModel;
    }

    @Override
    public CommentDtoResponse commentModelToCommentDto(CommentModel commentModel) {
        if ( commentModel == null ) {
            return null;
        }

        CommentDtoResponse commentDtoResponse = new CommentDtoResponse();

        commentDtoResponse.setNewsDtoResponse( newsModelToNewsDtoResponse( commentModel.getNewsModel() ) );
        commentDtoResponse.setId( commentModel.getId() );
        commentDtoResponse.setContent( commentModel.getContent() );
        commentDtoResponse.setCreateDate( commentModel.getCreateDate() );
        commentDtoResponse.setLastUpdateDate( commentModel.getLastUpdateDate() );

        return commentDtoResponse;
    }

    @Override
    public List<CommentDtoResponse> commentModelListToCommentDtoList(List<CommentModel> commentModelList) {
        if ( commentModelList == null ) {
            return null;
        }

        List<CommentDtoResponse> list = new ArrayList<CommentDtoResponse>( commentModelList.size() );
        for ( CommentModel commentModel : commentModelList ) {
            list.add( commentModelToCommentDto( commentModel ) );
        }

        return list;
    }

    @Override
    public CommentModel commentIdToCommentModel(Long commentId) {
        if ( commentId == null ) {
            return null;
        }

        CommentModel commentModel = new CommentModel();

        commentModel.setId( commentId );

        return commentModel;
    }

    @Override
    public NewsModel newsIdToNewsModel(Long newsId) {
        if ( newsId == null ) {
            return null;
        }

        NewsModel newsModel = new NewsModel();

        newsModel.setId( newsId );

        return newsModel;
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
}
