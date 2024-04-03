package com.mjc.school.controller.impl;

import com.mjc.school.controller.interfaces.AuthorControllerInterface;
import com.mjc.school.service.AuthorServiceInterface;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.dto.NewsDtoResponse;
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

import static com.mjc.school.service.utils.RestApiConst.AUTHOR_API_ROOT_PATH;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = AUTHOR_API_ROOT_PATH)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, value = "Operations for creating, updating, retrieving and deleting authors in the application")
public class AuthorController implements AuthorControllerInterface {
    private final AuthorServiceInterface authorService;
    private final HttpServletRequest httpServletRequest;
    private final RequestValidator requestValidator;

    @Autowired
    public AuthorController(AuthorServiceInterface authorService,
                            HttpServletRequest httpServletRequest,
                            RequestValidator requestValidator) {
        this.authorService = authorService;
        this.httpServletRequest = httpServletRequest;
        this.requestValidator = requestValidator;
    }

    @GetMapping
    @ApiOperation(value = "View all authors", response = CollectionModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all authors"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<CollectionModel<AuthorDtoResponse>> readAll(
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

        List<AuthorDtoResponse> authorDtoResponseList = authorService.readAll(pageNum, pageSize, sortBy);

        for (final AuthorDtoResponse authorDtoResponse: authorDtoResponseList) {
            Long authorId = authorDtoResponse.getId();
            Link selfLink = linkTo(AuthorController.class).slash(authorId).withSelfRel();
            authorDtoResponse.add(selfLink);
            if (!authorDtoResponse.getNewsDtoResponseList().isEmpty()) {
                for (NewsDtoResponse newsDto: authorDtoResponse.getNewsDtoResponseList()) {
                    Long newsId = newsDto.getId();
                    Link newsLink = linkTo(methodOn(NewsController.class)
                            .readById(newsId)).withRel("allNews");
                    authorDtoResponse.add(newsLink);
                }
            }
        }

        Link link = linkTo(AuthorController.class).withSelfRel();
        CollectionModel<AuthorDtoResponse> result = CollectionModel.of(authorDtoResponseList, link);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Get specific author by id", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the author with supplied id"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<AuthorDtoResponse> readById(@PathVariable Long id) {

        AuthorDtoResponse authorDtoResponse = authorService.readById(id);

        Long authorId = authorDtoResponse.getId();
        Link selfLink = linkTo(AuthorController.class).slash(authorId).withSelfRel();
        authorDtoResponse.add(selfLink);

        return new ResponseEntity<>(authorDtoResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    @ApiOperation(value = "Create an author", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Author has been successfully created"),
            @ApiResponse(code = 400, message = "Invalid data has been sent"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<AuthorDtoResponse> create(@RequestBody AuthorDtoRequest createRequest) {

        AuthorDtoResponse authorDtoResponse = authorService.create(createRequest);
        return new ResponseEntity<>(authorDtoResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id:\\d+}")
    @ApiOperation(value = "Update specific author by id", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Author has been successfully updated"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<AuthorDtoResponse> update(
            @PathVariable Long id, @RequestBody AuthorDtoRequest updateRequest) {

        updateRequest.setId(id);
        AuthorDtoResponse authorDtoResponse = authorService.update(updateRequest);
        return new ResponseEntity<>(authorDtoResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id:\\d+}")
    @ApiOperation(value = "Delete specific author by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Author has been successfully deleted"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void deleteById(@PathVariable Long id) {
        authorService.deleteById(id);
    }

    @PatchMapping(value = "/partial-update/{id:\\d+}")
    @ApiOperation(value = "Partially update specific author by id", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Author has been successfully partially updated"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<AuthorDtoResponse> partialUpdate(
            @PathVariable Long id, @RequestBody AuthorDtoRequest updateRequest) {

        AuthorDtoResponse authorDtoResponse = authorService.partialUpdate(id, updateRequest);
        return new ResponseEntity<>(authorDtoResponse, HttpStatus.OK);
    }

    // Get Author by part name
    @GetMapping(value = "/name/{name}")
    @ApiOperation(value = "Get author by part name", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the author by part name"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<AuthorDtoResponse> readByName(@PathVariable("name") String authorName) {

        AuthorDtoResponse authorDtoResponse = authorService.readByName(authorName);
        return new ResponseEntity<>(authorDtoResponse, HttpStatus.OK);
    }

    // Get Author by news id – return author by provided news id.
    @GetMapping(value = "/news/{newsId:\\d+}/authors")
    @ApiOperation(value = "Get author by news id", response = AuthorDtoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the author by news id"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<AuthorDtoResponse> getAuthorByNewsId(@PathVariable Long newsId) {

        AuthorDtoResponse authorDtoResponse = authorService.getAuthorByNewsId(newsId);
        return new ResponseEntity<>(authorDtoResponse, HttpStatus.OK);
    }

    // Get Authors with amount of written news. Sort by news amount Desc.
    @GetMapping(value = "/news-amount")
    @ApiOperation(value = "View the authors with written amount of news", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the authors with written amount of news"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request"),
    })
    @Override
    public ResponseEntity<List<AuthorDtoResponse>> getAuthorsByWrittenNews() {

        List<AuthorDtoResponse> authorDtoResponses = authorService.getAuthorsByWrittenNews();
        return new ResponseEntity<>(authorDtoResponses, HttpStatus.OK);
    }
}