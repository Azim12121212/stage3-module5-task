package com.mjc.school.service.validation;

import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.errorsexceptions.Errors;
import com.mjc.school.service.errorsexceptions.ValidatorException;
import org.springframework.stereotype.Component;


@Component
public class Validator {
    private static final int NEWS_TITLE_MIN = 5;
    private static final int NEWS_TITLE_MAX = 30;
    private static final int NEWS_CONTENT_MIN = 5;
    private static final int NEWS_CONTENT_MAX = 255;
    private static final int AUTHOR_NAME_MIN = 3;
    private static final int AUTHOR_NAME_MAX = 15;
    private static final int TAG_NAME_MIN = 3;
    private static final int TAG_NAME_MAX = 15;
    private static final int COMMENT_CONTENT_MIN = 5;
    private static final int COMMENT_CONTENT_MAX = 255;

    public void validateNewsDtoRequest(NewsDtoRequest newsDtoRequest) {
        validateNewsTitle(newsDtoRequest.getTitle());
        validateNewsContent(newsDtoRequest.getContent());
        validateAuthorId(newsDtoRequest.getAuthorId());
    }

    public void validateAuthorDtoRequest(AuthorDtoRequest authorDtoRequest) {
        validateAuthorName(authorDtoRequest.getName());
    }

    public void validateTagDtoRequest(TagDtoRequest tagDtoRequest) {
        validateTagName(tagDtoRequest.getName());
    }

    public void validateCommentDtoRequest(CommentDtoRequest commentDtoRequest) {
        validateCommentContent(commentDtoRequest.getContent());
        validateNewsId(commentDtoRequest.getNewsId());
    }

    public void validateNewsId(Long newsId) {
        if (newsId==null || newsId<1) {
            throw new ValidatorException(Errors.ERROR_NEWS_ID_VALUE.getErrorData(String.valueOf(newsId), false));
        }
    }

    public void validateNewsTitle(String title) {
        if (title!=null) {
            if (title.length() < NEWS_TITLE_MIN || title.length() > NEWS_TITLE_MAX) {
                throw new ValidatorException(Errors.ERROR_NEWS_TITLE_LENGTH.getErrorData(title, false));
            }
        }
    }

    public void validateNewsContent(String content) {
        if (content!=null) {
            if (content.length() < NEWS_CONTENT_MIN || content.length() > NEWS_CONTENT_MAX) {
                throw new ValidatorException(Errors.ERROR_NEWS_CONTENT_LENGTH.getErrorData(content, false));
            }
        }
    }

    public void validateAuthorId(Long authorId) {
        if (authorId==null || authorId<1) {
            throw new ValidatorException(Errors.ERROR_AUTHOR_ID_VALUE.getErrorData(String.valueOf(authorId), false));
        }
    }

    public void validateAuthorName(String name) {
        if (name!=null) {
            if (name.length() < AUTHOR_NAME_MIN || name.length() > AUTHOR_NAME_MAX) {
                throw new ValidatorException(Errors.ERROR_AUTHOR_NAME_LENGTH.getErrorData(name, false));
            }
        }
    }

    public void validateTagId(Long tagId) {
        if (tagId==null || tagId<1) {
            throw new ValidatorException(Errors.ERROR_TAG_ID_VALUE.getErrorData(String.valueOf(tagId), false));
        }
    }

    public void validateTagName(String name) {
        if (name!=null) {
            if (name.length() < TAG_NAME_MIN || name.length() > TAG_NAME_MAX) {
                throw new ValidatorException(Errors.ERROR_TAG_NAME_LENGTH.getErrorData(name, false));
            }
        }
    }

    public void validateCommentId(Long commentId) {
        if (commentId==null || commentId<1) {
            throw new ValidatorException(Errors.ERROR_COMMENT_ID_VALUE.getErrorData(String.valueOf(commentId), false));
        }
    }

    public void validateCommentContent(String commentContent) {
        if (commentContent!=null) {
            if (commentContent.length() < COMMENT_CONTENT_MIN || commentContent.length() > COMMENT_CONTENT_MAX) {
                throw new ValidatorException(Errors.ERROR_COMMENT_CONTENT_LENGTH.getErrorData(commentContent, false));
            }
        }
    }

    public void validateSortField(String sortField) {
        if (sortField != null) {
            if (sortField.equals("createdate") || sortField.equals("lastupdatedate")) {
                return;
            }
            throw new ValidatorException(Errors.ERROR_SORT_FIELD.getErrorData(sortField, false));
        }
    }

    public void validateSortOrder(String sortOrder) {
        if (sortOrder != null) {
            if (sortOrder.equals("asc") || sortOrder.equals("desc")) {
                return;
            }
            throw new ValidatorException(Errors.ERROR_SORT_ORDER.getErrorData(sortOrder, false));
        }
    }
}