package com.mjc.school.repository.impl;

import com.mjc.school.repository.AuthorRepositoryInterface;
import com.mjc.school.repository.NewsRepositoryInterface;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.utils.UtilsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepository implements AuthorRepositoryInterface {
    private final DateTimeFormatter MY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    @PersistenceContext
    private EntityManager entityManager;
    private final NewsRepositoryInterface newsRepository;

    @Autowired
    public AuthorRepository(NewsRepositoryInterface newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<AuthorModel> readAll(Integer pageNum, Integer pageSize, String sortBy) {

        String sortValue = UtilsRepository.parseSortValue(sortBy);
        String jpql = "SELECT a FROM AuthorModel a ORDER BY a." + sortValue;

        Query query = entityManager.createQuery(jpql);
        if (pageNum!=null && pageSize!=null) {
            query.setFirstResult((pageNum-1)*pageSize);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    @Override
    public Optional<AuthorModel> readById(Long id) {
        return Optional.ofNullable(entityManager.find(AuthorModel.class, id));
    }

    @Override
    public AuthorModel create(AuthorModel entity) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(MY_FORMAT));
        entity.setCreateDate(dateTime);
        entity.setLastUpdateDate(dateTime);
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public AuthorModel update(AuthorModel entity) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(MY_FORMAT));
        AuthorModel authorModel = null;
        if (existById(entity.getId())) {
            authorModel = entityManager.find(AuthorModel.class, entity.getId());
            authorModel.setName(entity.getName());
            authorModel.setLastUpdateDate(dateTime);
            entityManager.merge(authorModel);
        }
        return authorModel;
    }

    @Override
    public boolean deleteById(Long id) {
        if (existById(id)) {
            entityManager.remove(entityManager.find(AuthorModel.class, id));
            return true;
        }
        return false;
    }

    @Override
    public boolean existById(Long id) {
        return entityManager.find(AuthorModel.class, id)!=null;
    }

    @Override
    public List<AuthorModel> getAllAuthors() {
        return entityManager.createQuery("SELECT a FROM AuthorModel a",
                AuthorModel.class).getResultList();
    }

    @Override
    public AuthorModel partialUpdate(Long id, AuthorModel entity) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(MY_FORMAT));
        entity.setLastUpdateDate(dateTime);
        entityManager.merge(entity);
        return entity;
    }

    // Get Author by part name
    @Override
    public Optional<AuthorModel> readByName(String authorName) {
        AuthorModel authorModel = (AuthorModel) entityManager
                .createQuery("SELECT a FROM AuthorModel a WHERE a.name LIKE :name")
                .setParameter("name", "%" + authorName + "%")
                .getResultList().stream().findFirst().orElse(null);
        return Optional.ofNullable(authorModel);
    }

    // Get Author by news id – return author by provided news id.
    @Override
    public Optional<AuthorModel> getAuthorByNewsId(Long newsId) {
        Optional<NewsModel> newsModel = newsRepository.readById(newsId);
        if (newsModel.isPresent()) {
            return Optional.ofNullable(newsModel.get().getAuthorModel());
        }
        return null;
    }

    // Get Authors with amount of written news. Sort by news amount Desc.
    @Override
    public List<AuthorModel> getAuthorsByWrittenNews() {
        String jpql = "SELECT a FROM AuthorModel a JOIN " +
                "a.newsModelList n GROUP BY a ORDER BY COUNT(n) DESC";
        List<AuthorModel> authorModels = entityManager.createQuery(jpql).getResultList();
        return authorModels;
    }
}