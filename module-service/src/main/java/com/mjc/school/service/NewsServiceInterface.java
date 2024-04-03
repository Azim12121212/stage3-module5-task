package com.mjc.school.service;

import com.mjc.school.service.dto.*;
import com.mjc.school.service.utils.NewsParamsQuery;

import java.util.List;

public interface NewsServiceInterface extends BaseService<NewsDtoRequest, NewsDtoResponse, Long> {

    List<NewsDtoResponse> getAllNews();

    NewsDtoResponse partialUpdate(Long id, NewsDtoRequest newsDtoRequest);

    List<NewsDtoResponse> getNewsByParams(NewsParamsQuery newsParamsQuery);
}