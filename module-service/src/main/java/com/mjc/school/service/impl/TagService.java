package com.mjc.school.service.impl;

import com.mjc.school.repository.NewsRepositoryInterface;
import com.mjc.school.repository.TagRepositoryInterface;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.TagServiceInterface;
import com.mjc.school.service.annotation.ValidatingNewsId;
import com.mjc.school.service.annotation.ValidatingTag;
import com.mjc.school.service.annotation.ValidatingTagId;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.errorsexceptions.Errors;
import com.mjc.school.service.errorsexceptions.NotFoundException;
import com.mjc.school.service.mapper.NewsMapper;
import com.mjc.school.service.mapper.TagMapper;
import com.mjc.school.service.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagService implements TagServiceInterface {
    private final TagRepositoryInterface tagRepository;
    private final NewsRepositoryInterface newsRepository;
    private final TagMapper mapper;
    private final NewsMapper newsMapper;
    private final Validator validator;

    @Autowired
    public TagService(TagRepositoryInterface tagRepository, NewsRepositoryInterface newsRepository, TagMapper mapper,
                      NewsMapper newsMapper, Validator validator) {
        this.tagRepository = tagRepository;
        this.newsRepository = newsRepository;
        this.mapper = mapper;
        this.newsMapper = newsMapper;
        this.validator = validator;
    }

    @Override
    public List<TagDtoResponse> readAll(Integer pageNum, Integer pageSize, String sortBy) {

        if (sortBy.equals("id") || sortBy.startsWith("id:")
                || sortBy.equals("name") || sortBy.startsWith("name:")) {
            return mapper.tagModelListToTagDtoList(tagRepository.readAll(pageNum, pageSize, sortBy));
        } else {
            throw new NotFoundException(Errors.ERROR_REQUEST_PARAM_SORT.getErrorData(sortBy, true));
        }
    }

    @ValidatingTagId
    @Override
    public TagDtoResponse readById(Long id) {
        return tagRepository.readById(id)
                .map(mapper::tagModelToTagDto)
                .orElseThrow(() -> new NotFoundException(Errors.ERROR_TAG_ID_NOT_EXIST
                        .getErrorData(String.valueOf(id), true)));
    }

    @ValidatingTag
    @Transactional
    @Override
    public TagDtoResponse create(TagDtoRequest createRequest) {
        TagModel tagModel = tagRepository.create(mapper.tagDtoToTagModel(createRequest));
        return mapper.tagModelToTagDto(tagModel);
    }

    @ValidatingTag
    @Transactional
    @Override
    public TagDtoResponse update(TagDtoRequest updateRequest) {
        validator.validateTagId(updateRequest.getId());

        if (readById(updateRequest.getId())!=null) {
            TagModel tagModel = tagRepository.update(mapper.tagDtoToTagModel(updateRequest));
            return mapper.tagModelToTagDto(tagModel);
        } else {
            throw new NotFoundException(Errors.ERROR_TAG_ID_NOT_EXIST
                    .getErrorData(String.valueOf(updateRequest.getId()), true));
        }
    }

    @ValidatingTagId
    @Transactional
    @Override
    public boolean deleteById(Long id) {
        if (readById(id)!=null) {
            return tagRepository.deleteById(id);
        } else {
            return false;
        }
    }

    @Transactional
    @Override
    public TagDtoResponse partialUpdate(Long id, TagDtoRequest updateRequest) {
        validator.validateTagId(id);
        Optional<TagModel> existingTagModel = tagRepository.readById(id);

        if (existingTagModel.isPresent()) {
            if (updateRequest.getName()!=null) {
                validator.validateTagName(updateRequest.getName());
                existingTagModel.get().setName(updateRequest.getName());
            }
            TagModel tagModel = tagRepository.partialUpdate(id, existingTagModel.get());
            return mapper.tagModelToTagDto(tagModel);
        } else {
            throw new NotFoundException(Errors.ERROR_TAG_ID_NOT_EXIST
                    .getErrorData(String.valueOf(id), true));
        }
    }

    @Override
    public List<TagDtoResponse> getAllTags() {
        return mapper.tagModelListToTagDtoList(tagRepository.getAllTags());
    }

    // Get Tag by part name
    @Override
    public TagDtoResponse readByName(String tagName) {
        validator.validateTagName(tagName);
        return tagRepository.readByName(tagName)
                .map(mapper::tagModelToTagDto)
                .orElseThrow(() -> new NotFoundException(Errors.ERROR_TAG_PART_NAME_NOT_EXIST
                        .getErrorData(tagName, true)));
    }

    // Get Tags by news id – return tags by provided news id.
    @ValidatingNewsId
    @Override
    public Set<TagDtoResponse> getTagsByNewsId(Long newsId) {
        if (newsRepository.readById(newsId).isPresent()) {
            Set<TagModel> tagModelSet = tagRepository.getTagsByNewsId(newsId);
            if (tagModelSet!=null && !tagModelSet.isEmpty()) {
                return mapper.tagModelSetToTagDtoSet(tagModelSet);
            } else {
                throw new NotFoundException(Errors.ERROR_TAG_NEWS_ID_NOT_EXIST
                        .getErrorData(String.valueOf(newsId), true));
            }
        } else {
            throw new NotFoundException(Errors.ERROR_NEWS_ID_NOT_EXIST
                    .getErrorData(String.valueOf(newsId), true));
        }
    }
}