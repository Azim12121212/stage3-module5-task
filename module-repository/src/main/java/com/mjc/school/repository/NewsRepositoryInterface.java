package com.mjc.school.repository;

import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.utils.NewsParams;

import java.util.List;

public interface NewsRepositoryInterface extends BaseRepository<NewsModel, Long> {

    List<NewsModel> getAllNews();

    NewsModel partialUpdate(Long id, NewsModel entity);

    List<NewsModel> getNewsByParams(NewsParams newsParams);
}