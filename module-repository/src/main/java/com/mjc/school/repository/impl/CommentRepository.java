package com.mjc.school.repository.impl;

import com.mjc.school.repository.CommentRepositoryInterface;
import com.mjc.school.repository.NewsRepositoryInterface;
import com.mjc.school.repository.model.CommentModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.utils.CommentParams;
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
public class CommentRepository implements CommentRepositoryInterface {
    private final DateTimeFormatter MY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    @PersistenceContext
    private EntityManager entityManager;
    private final NewsRepositoryInterface newsRepository;

    @Autowired
    public CommentRepository(NewsRepositoryInterface newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<CommentModel> readAll(Integer pageNum, Integer pageSize, String sortBy) {

        String sortValue = UtilsRepository.parseSortValue(sortBy);
        String jpql = "SELECT c FROM CommentModel c ORDER BY c." + sortValue;

        Query query = entityManager.createQuery(jpql);
        if (pageNum!=null && pageSize!=null) {
            query.setFirstResult((pageNum-1)*pageSize);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    @Override
    public Optional<CommentModel> readById(Long id) {
        return Optional.ofNullable(entityManager.find(CommentModel.class, id));
    }

    @Override
    public CommentModel create(CommentModel entity) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(MY_FORMAT));
        entity.setCreateDate(dateTime);
        entity.setLastUpdateDate(dateTime);
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public CommentModel update(CommentModel entity) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(MY_FORMAT));
        CommentModel commentModel = null;
        if (existById(entity.getId())) {
            commentModel = entityManager.find(CommentModel.class, entity.getId());
            commentModel.setContent(entity.getContent());
            commentModel.setLastUpdateDate(dateTime);
            commentModel.setNewsModel(entity.getNewsModel());

            entityManager.merge(commentModel);
        }
        return commentModel;
    }

    @Override
    public boolean deleteById(Long id) {
        if (existById(id)) {
            entityManager.remove(entityManager.find(CommentModel.class, id));
            return true;
        }
        return false;
    }

    @Override
    public boolean existById(Long id) {
        return entityManager.find(CommentModel.class, id)!=null;
    }

    @Override
    public CommentModel partialUpdate(Long id, CommentModel entity) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(MY_FORMAT));
        entity.setLastUpdateDate(dateTime);
        entityManager.merge(entity);
        return entity;
    }

    // Get Comments by news id – return comments by provided news id.
    @Override
    public List<CommentModel> getCommentsByNewsId(Long newsId, CommentParams commentParams) {

        NewsModel newsModel = newsRepository.readById(newsId).get();

        if (newsModel!=null) {

            List<CommentModel> commentModelList = newsModel.getCommentModelList();

            UtilsRepository.sortCommentsByDate(commentModelList,
                    commentParams.getSortField(), commentParams.getSortOrder());

            return commentModelList;
        }

        return null;
    }
}