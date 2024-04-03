package com.mjc.school.controller.impl;

import com.mjc.school.controller.interfaces.CommentControllerInterface;
import com.mjc.school.service.CommentServiceInterface;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.utils.CommentParamsQuery;
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

import static com.mjc.school.service.utils.RestApiConst.COMMENTS_API_ROOT_PATH;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = COMMENTS_API_ROOT_PATH)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, value = "Operations for creating, updating, retrieving and deleting comments in the application")
public class CommentController implements CommentControllerInterface {
    private final CommentServiceInterface commentService;
    private final HttpServletRequest httpServletRequest;
    private final RequestValidator requestValidator;

    @Autowired
    public CommentController(CommentServiceInterface commentService,
                             HttpServletRequest httpServletRequest,
                             RequestValidator requestValidator) {
        this.commentService = commentService;
        this.httpServletRequest = httpServletRequest;
        this.requestValidator = requestValidator;
    }

    @GetMapping
    @ApiOperation(value = "View all comments", response = CollectionModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all comments"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<CollectionModel<CommentDtoResponse>> readAll(
            @RequestParam(required = false, name = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, name = "size", defaultValue = "5") Integer pageSize,
            @RequestParam(required = false, name = "sort", defaultValue = "id") String sortBy) {

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

        List<CommentDtoResponse> commentDtoResponseList = commentService.readAll(pageNum, pageSize, sortBy);

        for (final CommentDtoResponse commentDtoResponse: commentDtoResponseList) {
            Long commentId = commentDtoResponse.getId();
            Link selfLink = linkTo(CommentController.class).slash(commentId).withSelfRel();
            commentDtoResponse.add(selfLink);
            Long newsId = commentDtoResponse.getNewsDtoResponse().getId();
            Link newsLink = linkTo(methodOn(NewsController.class).readById(newsId)).withRel("news");
            commentDtoResponse.add(newsLink);
        }

        Link link = linkTo(CommentController.class).withSelfRel();
        CollectionModel<CommentDtoResponse> result = CollectionModel.of(commentDtoResponseList, link);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Get specific comment by id", response = CommentDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the comment with supplied id"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<CommentDtoResponse> readById(@PathVariable Long id) {
        CommentDtoResponse commentDtoResponse = commentService.readById(id);
        return new ResponseEntity<>(commentDtoResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    @ApiOperation(value = "Create a comment", response = CommentDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Comment has been successfully created"),
            @ApiResponse(code = 400, message = "Invalid data has been sent"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<CommentDtoResponse> create(@RequestBody CommentDtoRequest createRequest) {

        CommentDtoResponse commentDtoResponse = commentService.create(createRequest);
        return new ResponseEntity<>(commentDtoResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id:\\d+}")
    @ApiOperation(value = "Update specific comment by id", response = CommentDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment has been successfully updated"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<CommentDtoResponse> update(
            @PathVariable Long id, @RequestBody CommentDtoRequest updateRequest) {

        updateRequest.setId(id);
        CommentDtoResponse commentDtoResponse = commentService.update(updateRequest);
        return new ResponseEntity<>(commentDtoResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Delete specific comment by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Comment has been successfully deleted"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void deleteById(@PathVariable Long id) {
        commentService.deleteById(id);
    }

    @PatchMapping(value = "/partial-update/{id:\\d+}")
    @ApiOperation(value = "Partially update specific comment by id", response = CommentDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment has been successfully partially updated"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<CommentDtoResponse> partialUpdate(
            @PathVariable Long id, @RequestBody CommentDtoRequest updateRequest) {

        CommentDtoResponse commentDtoResponse = commentService.partialUpdate(id, updateRequest);
        return new ResponseEntity<>(commentDtoResponse, HttpStatus.OK);
    }

    // Get Comments by news id – return comments by provided news id.
    @GetMapping(value = "/news/{newsId:\\d+}/comments")
    @ApiOperation(value = "View the comments by news id", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the comments by given news id"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<List<CommentDtoResponse>> getCommentsByNewsId(
            @PathVariable Long newsId, @RequestBody CommentParamsQuery commentParamsQuery) {

        List<CommentDtoResponse> commentDtoResponses =
                commentService.getCommentsByNewsId(newsId, commentParamsQuery);
        return new ResponseEntity<>(commentDtoResponses, HttpStatus.OK);
    }
}