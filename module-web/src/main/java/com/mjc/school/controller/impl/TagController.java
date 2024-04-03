package com.mjc.school.controller.impl;

import com.mjc.school.controller.interfaces.TagControllerInterface;
import com.mjc.school.service.TagServiceInterface;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.validation.RequestValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mjc.school.service.utils.RestApiConst.TAG_API_ROOT_PATH;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = TAG_API_ROOT_PATH)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, value = "Operations for creating, updating, retrieving and deleting tags in the application")
public class TagController implements TagControllerInterface {
    private final TagServiceInterface tagService;
    private final HttpServletRequest httpServletRequest;
    private final RequestValidator requestValidator;

    @Autowired
    public TagController(TagServiceInterface tagService,
                         HttpServletRequest httpServletRequest,
                         RequestValidator requestValidator) {
        this.tagService = tagService;
        this.httpServletRequest = httpServletRequest;
        this.requestValidator = requestValidator;
    }

    @GetMapping
    @ApiOperation(value = "View all tags", response = CollectionModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all tags"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<CollectionModel<TagDtoResponse>> readAll(
            @RequestParam(required = false, name = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, name = "size", defaultValue = "5") Integer pageSize,
            @RequestParam(required = false, name = "sort", defaultValue = "name") String sortBy) {

        if (httpServletRequest.getQueryString()!=null) {
            Map<String, String> paramNameValues =
                    requestValidator.validateRequestParams(httpServletRequest.getQueryString());

            for (String paramName : paramNameValues.keySet()) {
                if (paramName.equals("page")) {
                    pageNum = Integer.valueOf(paramNameValues.get(paramName));
                } else if (paramName.equals("size")) {
                    pageSize = Integer.valueOf(paramNameValues.get(paramName));
                } else if (paramName.equals("sort")) {
                    sortBy = paramNameValues.get(paramName);
                }
            }
        }

        List<TagDtoResponse> tagDtoResponseList = tagService.readAll(pageNum, pageSize, sortBy);

        for (final TagDtoResponse tagDtoResponse: tagDtoResponseList) {
            Long tagId = tagDtoResponse.getId();
            Link selfLink = linkTo(TagController.class).slash(tagId).withSelfRel();
            tagDtoResponse.add(selfLink);
            if (!tagDtoResponse.getNewsDtoResponseSet().isEmpty()) {
                for (NewsDtoResponse newsDto: tagDtoResponse.getNewsDtoResponseSet()) {
                    Long newsId = newsDto.getId();
                    Link newsLink = linkTo(methodOn(NewsController.class).readById(newsId)).withRel("allNews");
                    tagDtoResponse.add(newsLink);
                }
            }
        }

        Link link = linkTo(TagController.class).withSelfRel();
        CollectionModel<TagDtoResponse> result = CollectionModel.of(tagDtoResponseList, link);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Get specific tag by id", response = TagDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the tag with supplied id"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<TagDtoResponse> readById(@PathVariable Long id) {

        TagDtoResponse tagDtoResponse = tagService.readById(id);
        return new ResponseEntity<>(tagDtoResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    @ApiOperation(value = "Create a tag", response = TagDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Tag has been successfully created"),
            @ApiResponse(code = 400, message = "Invalid data has been sent"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<TagDtoResponse> create(@RequestBody TagDtoRequest createRequest) {

        TagDtoResponse tagDtoResponse = tagService.create(createRequest);
        return new ResponseEntity<>(tagDtoResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id:\\d+}")
    @ApiOperation(value = "Update specific tag by id", response = TagDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tag has been successfully updated"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<TagDtoResponse> update(
            @PathVariable Long id, @RequestBody TagDtoRequest updateRequest) {

        updateRequest.setId(id);
        TagDtoResponse tagDtoResponse = tagService.update(updateRequest);
        return new ResponseEntity<>(tagDtoResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Delete specific tag by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Tag has been successfully deleted"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
    }

    @PatchMapping(value = "/partial-update/{id:\\d+}")
    @ApiOperation(value = "Partially update specific tag by id", response = TagDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tag has been successfully partially updated"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<TagDtoResponse> partialUpdate(
            @PathVariable Long id, @RequestBody TagDtoRequest updateRequest) {

        TagDtoResponse tagDtoResponse = tagService.partialUpdate(id, updateRequest);
        return new ResponseEntity<>(tagDtoResponse, HttpStatus.OK);
    }

    // Get Tag by part name
    @GetMapping(value = "/name/{name}")
    @ApiOperation(value = "Get tag by part name", response = TagDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the tag by part name"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<TagDtoResponse> readByName(@PathVariable("name") String tagName) {

        TagDtoResponse tagDtoResponse = tagService.readByName(tagName);
        return new ResponseEntity<>(tagDtoResponse, HttpStatus.OK);
    }

    // Get Tags by news id – return tags by provided news id.
    @GetMapping(value = "/news/{newsId:\\d+}/tags")
    @ApiOperation(value = "Get the tags by news id", response = Set.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the tags by given news id"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<Set<TagDtoResponse>> getTagsByNewsId(@PathVariable Long newsId) {

        Set<TagDtoResponse> tagDtoResponses = tagService.getTagsByNewsId(newsId);
        return new ResponseEntity<>(tagDtoResponses, HttpStatus.OK);
    }
}