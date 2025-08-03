package api.web_blogging.uz.repository;

import api.web_blogging.uz.dto.FilterResultDTO;
import api.web_blogging.uz.dto.post.PostFilterDTO;
import api.web_blogging.uz.entity.PostEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomRepository {
    @Autowired
    private EntityManager entityManager;

    public FilterResultDTO<PostEntity> filter(PostFilterDTO filter, int page, int size) {
        StringBuilder queryBuilder = new StringBuilder(" where p.visible = true ");


        Map<String, Object> params = new HashMap<String, Object>();

        if (filter.getQuery() != null) {
            queryBuilder.append(" AND lower(p.title) like :query ");
            params.put("query", "%" + filter.getQuery().toLowerCase() + "%");
        }
//        queryBuilder.append(" ORDER BY p.createdAt DESC ");

        StringBuilder selectBuilder = new StringBuilder(" Select p From PostEntity p ")
                .append(queryBuilder)
                .append(" ORDER BY p.createdAt DESC ");
        StringBuilder countBuilder = new StringBuilder(" Select count(p) From PostEntity p ")
                .append(queryBuilder);

        Query selectQuery = entityManager.createQuery(selectBuilder.toString());
        selectQuery.setFirstResult((page) * size);
        selectQuery.setMaxResults(size);
        params.forEach(selectQuery::setParameter);

        List<PostEntity> posts = selectQuery.getResultList();

//        count
        Query countQuery = entityManager.createQuery(countBuilder.toString());
        params.forEach(countQuery::setParameter);
        Long count = (Long) countQuery.getSingleResult();

        return new FilterResultDTO<>(posts, count);
    }
}
