package com.mjc.school.service.aspects;

import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.validation.Validator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidatingAspect {
    private final Validator validator;

    @Autowired
    public ValidatingAspect(Validator validator) {
        this.validator = validator;
    }

    @Before("@annotation(com.mjc.school.service.annotation.ValidatingNews) && args(newsDtoRequest)")
    public void validateNewsDtoRequest(NewsDtoRequest newsDtoRequest) {
        validator.validateNewsDtoRequest(newsDtoRequest);
    }

    @Before("@annotation(com.mjc.school.service.annotation.ValidatingAuthor) && args(authorDtoRequest)")
    public void validateAuthorDtoRequest(AuthorDtoRequest authorDtoRequest) {
        validator.validateAuthorDtoRequest(authorDtoRequest);
    }

    @Before("@annotation(com.mjc.school.service.annotation.ValidatingTag) && args(tagDtoRequest)")
    public void validateTagDtoRequest(TagDtoRequest tagDtoRequest) {
        validator.validateTagDtoRequest(tagDtoRequest);
    }

    @Before("@annotation(com.mjc.school.service.annotation.ValidatingComment) && args(commentDtoRequest)")
    public void validateCommentDtoRequest(CommentDtoRequest commentDtoRequest) {
        validator.validateCommentDtoRequest(commentDtoRequest);
    }

    @Before("@annotation(com.mjc.school.service.annotation.ValidatingNewsId) && args(id)")
    public void validateNewsDtoId(Long id) {
        validator.validateNewsId(id);
    }

    @Before("@annotation(com.mjc.school.service.annotation.ValidatingAuthorId) && args(id)")
    public void validateAuthorDtoId(Long id) {
        validator.validateAuthorId(id);
    }

    @Before("@annotation(com.mjc.school.service.annotation.ValidatingTagId) && args(id)")
    public void validateTagDtoId(Long id) {
        validator.validateTagId(id);
    }

    @Before("@annotation(com.mjc.school.service.annotation.ValidatingCommentId) && args(id)")
    public void validateCommentDtoId(Long id) {
        validator.validateCommentId(id);
    }
}