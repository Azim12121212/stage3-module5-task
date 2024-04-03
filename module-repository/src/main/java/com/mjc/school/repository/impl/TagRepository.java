package com.mjc.school.repository.impl;

import com.mjc.school.repository.NewsRepositoryInterface;
import com.mjc.school.repository.TagRepositoryInterface;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.repository.utils.UtilsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Repository
public class TagRepository implements TagRepositoryInterface {
    @PersistenceContext
    private EntityManager entityManager;
    private final NewsRepositoryInterface newsRepository;

    @Autowired
    public TagRepository(NewsRepositoryInterface newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<TagModel> readAll(Integer pageNum, Integer pageSize, String sortBy) {

        String sortValue = UtilsRepository.parseSortValue(sortBy);
        String jpql = "SELECT t FROM TagModel t ORDER BY t." + sortValue;

        Query query = entityManager.createQuery(jpql);
        if (pageNum!=null && pageSize!=null) {
            query.setFirstResult((pageNum-1)*pageSize);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    @Override
    public Optional<TagModel> readById(Long id) {
        return Optional.ofNullable(entityManager.find(TagModel.class, id));
    }

    @Override
    public TagModel create(TagModel entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public TagModel update(TagModel entity) {
        TagModel tagModel = null;
        if (existById(entity.getId())) {
            tagModel = entityManager.find(TagModel.class, entity.getId());
            tagModel.setName(entity.getName());
            entityManager.merge(tagModel);
        }
        return tagModel;
    }

    @Override
    public boolean deleteById(Long id) {
        if (existById(id)) {
            entityManager.remove(entityManager.find(TagModel.class, id));
            return true;
        }
        return false;
    }

    @Override
    public boolean existById(Long id) {
        return entityManager.find(TagModel.class, id)!=null;
    }

    @Override
    public TagModel partialUpdate(Long id, TagModel entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public List<TagModel> getAllTags() {
        return entityManager.createQuery("SELECT t FROM TagModel t", TagModel.class)
                .getResultList();
    }

    // Get Tag by part name
    @Override
    public Optional<TagModel> readByName(String tagName) {
        TagModel tagModel = (TagModel) entityManager
                .createQuery("SELECT t FROM TagModel t WHERE t.name LIKE :name")
                .setParameter("name", "%" + tagName + "%")
                .getResultList().stream().findFirst().orElse(null);
        return Optional.ofNullable(tagModel);
    }

    // Get Tags by news id – return tags by provided news id.
    @Override
    public Set<TagModel> getTagsByNewsId(Long newsId) {
        NewsModel newsModel = newsRepository.readById(newsId).get();
        if (newsModel!=null) {
            return newsModel.getTagModelSet();
        }
        return null;
    }
}