package kun.uz.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import kun.uz.dto.filter.CommentFilterDTO;
import kun.uz.dto.filter.FilterResultDTO;
import kun.uz.entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomCommentRepository {

    @Autowired
    private EntityManager entityManager;

    public FilterResultDTO<CommentEntity> filter(Integer page, Integer size, CommentFilterDTO dto) {

        StringBuilder selectBuilder = new StringBuilder("From CommentEntity as c where 1=1 ");
        StringBuilder countBuilder = new StringBuilder("select count(*) from CommentEntity as c where 1=1 ");
        StringBuilder condition = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if (dto.getId() != null) {
            condition.append(" and c.id = :id ");
            params.put("id", dto.getId());
        }
        if (dto.getArticleId() != null) {
            condition.append(" and c.article_id = :article_id ");
            params.put("article_id", dto.getArticleId());
        }
        if (dto.getProfileId() != null) {
            condition.append(" and c.profile_id = :profile_id ");
            params.put("profile_id", dto.getProfileId());
        }
        if (dto.getFrom() != null) {
            condition.append(" and c.from >= :from ");
            LocalDateTime date = LocalDateTime.of(dto.getFrom(), LocalTime.MIN);
            params.put("from", date);
        }
        if (dto.getTo() != null) {
            condition.append(" and c.to <= :to ");
            LocalDateTime date = LocalDateTime.of(dto.getTo(), LocalTime.MAX);
            params.put("to", date);
        }

        selectBuilder.append(condition.toString());
        selectBuilder.append(" order by c.createdDate desc ");
        Query selectQuery = entityManager.createQuery(selectBuilder.toString(), CommentEntity.class);
        selectQuery.setFirstResult(page * size); //offset
        selectQuery.setMaxResults(size); //size

        countBuilder.append(condition.toString());
        Query queryCount = entityManager.createQuery(countBuilder.toString());

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            selectQuery.setParameter(entry.getKey(), entry.getValue());
            queryCount.setParameter(entry.getKey(), entry.getValue());
        }

        List<CommentEntity> comments = selectQuery.getResultList();
        Long total = (Long) queryCount.getSingleResult();

        return new FilterResultDTO<CommentEntity>(comments, total);
    }

}
