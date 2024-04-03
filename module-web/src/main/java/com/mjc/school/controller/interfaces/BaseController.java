package com.mjc.school.controller.interfaces;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BaseController<T, R, K> {

    ResponseEntity<CollectionModel<R>> readAll(Integer pageNum, Integer pageSize, String sortBy);

    ResponseEntity<R> readById(K id);

    ResponseEntity<R> create(T createRequest);

    ResponseEntity<R> update(K id, T updateRequest);

    void deleteById(K id);
}