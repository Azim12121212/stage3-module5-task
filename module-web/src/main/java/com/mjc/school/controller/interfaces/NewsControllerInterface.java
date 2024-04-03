package com.mjc.school.controller.interfaces;

import com.mjc.school.service.dto.*;
import com.mjc.school.service.utils.NewsParamsQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NewsControllerInterface extends BaseController<NewsDtoRequest, NewsDtoResponse, Long> {

    ResponseEntity<NewsDtoResponse> partialUpdate(Long id, NewsDtoRequest newsDtoRequest);

    ResponseEntity<List<NewsDtoResponse>> getNewsByParams(NewsParamsQuery newsParamsQuery);
}