package com.mjc.school.controller.impl;

import com.mjc.school.controller.interfaces.NewsControllerInterface;
import com.mjc.school.service.NewsServiceInterface;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.utils.NewsParamsQuery;
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

import static com.mjc.school.service.utils.RestApiConst.NEWS_API_ROOT_PATH;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = NEWS_API_ROOT_PATH)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, value = "Operations for creating, updating, retrieving and deleting news in the application")
public class NewsController implements NewsControllerInterface {
    private final NewsServiceInterface newsService;
    private final HttpServletRequest httpServletRequest;
    private final RequestValidator requestValidator;

    @Autowired
    public NewsController(NewsServiceInterface newsService,
                          HttpServletRequest httpServletRequest,
                          RequestValidator requestValidator) {
        this.newsService = newsService;
        this.httpServletRequest = httpServletRequest;
        this.requestValidator = requestValidator;
    }

    @GetMapping
    @ApiOperation(value = "View all news", response = CollectionModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all news"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<CollectionModel<NewsDtoResponse>> readAll(
            @RequestParam(required = false, name = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, name = "size", defaultValue = "5") Integer pageSize,
            @RequestParam(required = false, name = "sort", defaultValue = "title") String sortBy) {

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

        List<NewsDtoResponse> newsDtoResponseList = newsService.readAll(pageNum, pageSize, sortBy);

        for (final NewsDtoResponse newsDtoResponse: newsDtoResponseList) {
            Long newsId = newsDtoResponse.getId();
            Link selfLink = linkTo(NewsController.class).slash(newsId).withSelfRel();
            newsDtoResponse.add(selfLink);
            Long authorId = newsDtoResponse.getAuthorDtoResponse().getId();
            Link authorLink = linkTo(methodOn(AuthorController.class).readById(authorId)).withRel("author");
            newsDtoResponse.add(authorLink);
            if (!newsDtoResponse.getTagDtoResponseSet().isEmpty()) {
                for (TagDtoResponse tagDto: newsDtoResponse.getTagDtoResponseSet()) {
                    Long tagId = tagDto.getId();
                    Link tagLink = linkTo(methodOn(TagController.class).readById(tagId)).withRel("allTags");
                    newsDtoResponse.add(tagLink);
                }
            }
            if (!newsDtoResponse.getCommentDtoResponseList().isEmpty()) {
                for (CommentDtoResponse commentDto: newsDtoResponse.getCommentDtoResponseList()) {
                    Long commentId = commentDto.getId();
                    Link commentLink = linkTo(methodOn(CommentController.class)
                            .readById(commentId)).withRel("allComments");
                    newsDtoResponse.add(commentLink);
                }
            }
        }

        Link link = linkTo(NewsController.class).withSelfRel();
        CollectionModel<NewsDtoResponse> result = CollectionModel.of(newsDtoResponseList, link);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Get specific news by id", response = NewsDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the news with supplied id"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<NewsDtoResponse> readById(@PathVariable Long id) {

        NewsDtoResponse newsDtoResponse = newsService.readById(id);
        return new ResponseEntity<>(newsDtoResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    @ApiOperation(value = "Create a news", response = NewsDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "News has been successfully created"),
            @ApiResponse(code = 400, message = "Invalid data has been sent"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<NewsDtoResponse> create(@RequestBody NewsDtoRequest createRequest) {

        NewsDtoResponse newsDtoResponse = newsService.create(createRequest);
        return new ResponseEntity<>(newsDtoResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id:\\d+}")
    @ApiOperation(value = "Update specific news by id", response = NewsDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "News has been successfully updated"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<NewsDtoResponse> update(
            @PathVariable Long id, @RequestBody NewsDtoRequest updateRequest) {

        updateRequest.setId(id);
        NewsDtoResponse newsDtoResponse = newsService.update(updateRequest);
        return new ResponseEntity<>(newsDtoResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Delete specific news by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "News has been successfully deleted"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void deleteById(@PathVariable Long id) {
        newsService.deleteById(id);
    }

    @PatchMapping(value = "/partial-update/{id:\\d+}")
    @ApiOperation(value = "Partially update specific news by id", response = NewsDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "News has been successfully partially updated"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<NewsDtoResponse> partialUpdate(
            @PathVariable Long id, @RequestBody NewsDtoRequest newsDtoRequest) {

        NewsDtoResponse newsDtoResponse = newsService.partialUpdate(id, newsDtoRequest);
        return new ResponseEntity<>(newsDtoResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/params")
    @ApiOperation(value = "Get specific news by params", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the news by given parameters"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<List<NewsDtoResponse>> getNewsByParams(@RequestBody NewsParamsQuery newsParamsQuery) {

        List<NewsDtoResponse> newsDtoResponses = newsService.getNewsByParams(newsParamsQuery);
        return new ResponseEntity<>(newsDtoResponses, HttpStatus.OK);
    }
}