package kun.uz.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import kun.uz.dto.filter.FilterDTO;
import kun.uz.dto.filter.FilterResultDTO;
import kun.uz.entity.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomProfileRepository {
    @Autowired
    private EntityManager entityManager;

    public FilterResultDTO<ProfileEntity> filter(int page, Integer size, FilterDTO filter) {

        StringBuilder condition = new StringBuilder();
        Map<String,Object> params = new HashMap<>();

        if(filter.getName()!=null){
            condition.append("and p.name ilike : name ");
            params.put("name","%" + filter.getName()+"%");
        }

        if(filter.getSurname()!=null){
            condition.append("and p.surname ilike : surname ");
            params.put("surname","%" + filter.getSurname()+"%");
        }
        if(filter.getRole()!=null){
            condition.append("and p.role ilike : role ");
            params.put("role","%" + filter.getRole()+"%");
        }
        if(filter.getLogin()!=null){
            condition.append("and p.login ilike : login ");
            params.put("login","%" + filter.getLogin()+"%");
        }
        if(filter.getFrom()!=null) {
            LocalDateTime fromDateTime = LocalDateTime.of(filter.getFrom(), LocalTime.MIN);
            condition.append("and p.createdDate>=:from ");
            params.put("from", fromDateTime);
        }
        if(filter.getTo()!=null) {
            condition.append("and p.createdDate<=:to ");
            LocalDateTime toDateTime = LocalDateTime.of(filter.getTo(), LocalTime.MAX);
            params.put("to", toDateTime);
        }
        StringBuilder selectBuilder = new StringBuilder("From ProfileEntity as p where 1=1 ");
        selectBuilder.append(condition);
        StringBuilder countBuilder = new StringBuilder("select count(*) from ProfileEntity as p where 1=1 ");
        countBuilder.append(condition);

        Query selectQuery = entityManager.createQuery(selectBuilder.toString(), ProfileEntity.class);
        selectQuery.setFirstResult(page*size);
        selectQuery.setMaxResults(size);
        Query countQuery = entityManager.createQuery(countBuilder.toString());

        for(Map.Entry<String,Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(),entry.getValue());
            selectQuery.setParameter(entry.getKey(),entry.getValue());
        }

        List<ProfileEntity> list = selectQuery.getResultList();
        long total = (long) countQuery.getSingleResult();

        return new FilterResultDTO<>(list,total);




    }
}
