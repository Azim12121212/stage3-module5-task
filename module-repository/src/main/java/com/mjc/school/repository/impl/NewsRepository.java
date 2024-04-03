package com.mjc.school.repository.impl;

import com.mjc.school.repository.NewsRepositoryInterface;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.utils.NewsParams;
import com.mjc.school.repository.utils.UtilsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class NewsRepository implements NewsRepositoryInterface {
    private final DateTimeFormatter MY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public NewsRepository() {
    }

    @Override
    public List<NewsModel> readAll(Integer pageNum, Integer pageSize, String sortBy) {

        String sortValue = UtilsRepository.parseSortValue(sortBy);
        String jpql = "SELECT n FROM NewsModel n ORDER BY n." + sortValue;

        Query query = entityManager.createQuery(jpql);
        if (pageNum!=null && pageSize!=null) {
            query.setFirstResult((pageNum-1)*pageSize);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    @Override
    public Optional<NewsModel> readById(Long id) {
        return Optional.ofNullable(entityManager.find(NewsModel.class, id));
    }

    @Override
    public NewsModel create(NewsModel entity) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(MY_FORMAT));
        entity.setCreateDate(dateTime);
        entity.setLastUpdateDate(dateTime);
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public NewsModel update(NewsModel entity) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(MY_FORMAT));
        NewsModel newsModel = null;
        if (existById(entity.getId())) {
            newsModel = entityManager.find(NewsModel.class, entity.getId());
            newsModel.setTitle(entity.getTitle());
            newsModel.setContent(entity.getContent());
            newsModel.setLastUpdateDate(dateTime);
            newsModel.setAuthorModel(entity.getAuthorModel());
            newsModel.setTagModelSet(entity.getTagModelSet());

            entityManager.merge(newsModel);
        }
        return newsModel;
    }

    @Override
    public boolean deleteById(Long id) {
        if (existById(id)) {
            entityManager.createQuery("DELETE FROM NewsModel n WHERE n.id = :id")
                    .setParameter("id", id).executeUpdate();
            return true;
        }
        return false;
    }

    @Override
    public boolean existById(Long id) {
        return entityManager.find(NewsModel.class, id)!=null;
    }

    @Override
    public List<NewsModel> getAllNews() {
        return entityManager.createQuery("SELECT n FROM NewsModel n", NewsModel.class)
                .getResultList();
    }

    @Override
    public NewsModel partialUpdate(Long id, NewsModel entity) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(MY_FORMAT));
        entity.setLastUpdateDate(dateTime);
        entityManager.merge(entity);
        return entity;
    }

    // Get news by params (tag ids, tag names, author name, title, content).
    @Override
    public List<NewsModel> getNewsByParams(NewsParams newsParams) {

        List<NewsModel> newsModelList = new ArrayList<>();
        Set<NewsModel> newsModelSet = new HashSet<>();

        if (newsParams.getTagIds()!=null && !newsParams.getTagIds().isEmpty()) {
            for (Long tagId: newsParams.getTagIds()) {
                String jpql = "SELECT n FROM NewsModel n JOIN FETCH n.tagModelSet t WHERE t.id = :tagId";
                List<NewsModel> news = entityManager.createQuery(jpql)
                        .setParameter("tagId", tagId)
                        .getResultList();
                newsModelSet.addAll(news);
            }
        }

        if (newsParams.getTagNames()!=null && !newsParams.getTagNames().isEmpty()) {
            for (String tagName: newsParams.getTagNames()) {
                String jpql = "SELECT n FROM NewsModel n JOIN FETCH n.tagModelSet t WHERE t.name LIKE :tagName";
                List<NewsModel> news = entityManager.createQuery(jpql)
                        .setParameter("tagName", "%"+tagName+"%")
                        .getResultList();
                newsModelSet.addAll(news);
            }
        }

        if (newsParams.getAuthorName()!=null && newsParams.getAuthorName()!="") {
            AuthorModel authorModel = (AuthorModel) entityManager
                    .createQuery("SELECT a FROM AuthorModel a WHERE a.name LIKE :name")
                    .setParameter("name", "%" + newsParams.getAuthorName() + "%")
                    .getResultList().stream().findFirst().orElse(null);
            if (authorModel!=null) {
                newsModelSet.addAll(authorModel.getNewsModelList());
            }
        }

        if (newsParams.getTitle()!=null && newsParams.getTitle()!="") {
            String jpql = "SELECT n FROM NewsModel n WHERE n.title LIKE :title";
            List<NewsModel> news = entityManager.createQuery(jpql)
                    .setParameter("title", "%" + newsParams.getTitle() + "%")
                    .getResultList();
            newsModelSet.addAll(news);
        }

        if (newsParams.getContent()!=null && newsParams.getContent()!="") {
            String jpql = "SELECT n FROM NewsModel n WHERE n.content LIKE :content";
            List<NewsModel> news = entityManager.createQuery(jpql)
                    .setParameter("content", "%" + newsParams.getContent() + "%")
                    .getResultList();
            newsModelSet.addAll(news);
        }

        newsModelList.addAll(newsModelSet);
        UtilsRepository.sortNewsByDate(newsModelList, newsParams.getSortField(), newsParams.getSortOrder());

        return newsModelList;
    }
}