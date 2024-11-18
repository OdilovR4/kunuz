package kun.uz.service;

import kun.uz.entity.IpArticleEntity;
import kun.uz.repository.IpArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class IpArticleService {
    @Autowired
    private IpArticleRepository ipArticleRepository;

    public boolean isExistIp(String ip, String articleId) {
        IpArticleEntity entity = ipArticleRepository.isExist(ip, articleId);
        if (entity != null) {
            return true;
        }
        entity = new IpArticleEntity();
        entity.setArticleId(articleId);
        entity.setIp(ip);
        entity.setCreatedTime(LocalDateTime.now());
        ipArticleRepository.save(entity);
        return false;
    }
}
