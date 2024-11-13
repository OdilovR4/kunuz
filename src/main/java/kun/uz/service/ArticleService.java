package kun.uz.service;

import jakarta.servlet.http.HttpServletRequest;
import kun.uz.dto.article.*;
import kun.uz.entity.ArticleEntity;
import kun.uz.entity.ArticleLikeEntity;
import kun.uz.enums.ArticleStatus;
import kun.uz.enums.LikeStatus;
import kun.uz.exceptions.ResourceNotFoundException;
import kun.uz.repository.ArticleRepository;
import kun.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleTypeArticleService articleTypeArticleService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private IpArticleService ipArticleService;
    @Autowired
    private ArticleLikeService articleLikeService;

    public ArticleCreationDTO createArticle(ArticleCreationDTO creationDTO) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle(creationDTO.getTitle());
        articleEntity.setContent(creationDTO.getContent());
        articleEntity.setModeratorId(SpringSecurityUtil.getCurrentUser().getId());
        articleEntity.setDescription(creationDTO.getDescription());
        articleEntity.setCategoryId(creationDTO.getCategoryId());
        articleEntity.setPhotoId(creationDTO.getPhotoId());
        articleEntity.setCreatedDate(LocalDateTime.now());
        articleEntity.setRegionId(creationDTO.getRegionId());
        articleEntity.setStatus(ArticleStatus.NOT_PUBLISHED);
        articleEntity.setVisible(Boolean.TRUE);
        articleRepository.save(articleEntity);
        articleTypeArticleService.addArticleType(articleEntity.getId(), creationDTO.getArticleType());
        creationDTO.setId(articleEntity.getId());

        return creationDTO;
    }

    public boolean update(String id, UpdateArticleDTO dto) {
        ArticleEntity article = articleRepository.getById(id);
        if (article == null) {
            throw new ResourceNotFoundException("Article Not Found");
        }
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setDescription(dto.getDescription());
        article.setCategoryId(dto.getCategoryId());
        article.setCreatedDate(LocalDateTime.now());
        article.setRegionId(dto.getRegionId());
        article.setPhotoId(dto.getPhotoId());

        return true;
    }

    public ArticleDTO getById(String id) {
        ArticleEntity entity = articleRepository.getById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Article Not Found");
        }

        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setDescription(entity.getDescription());
        dto.setSharedCount(entity.getSharedCount());
        dto.setViewCount(entity.getViewCount());
        dto.setCategory(categoryService.getById(entity.getCategoryId()));
        dto.setRegion(regionService.getById(entity.getRegionId()));
        dto.setModerator(profileService.getProfile(entity.getModeratorId()));
        dto.setPhoto(attachService.getDto(entity.getPhotoId()));
        dto.setArticleType(articleTypeArticleService.getArticleList(entity.getId()));
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public Boolean delete(String id) {
        return articleRepository.deleteArticle(id) == 1;
    }

    public Boolean changeStatus(String id) {
        ArticleEntity article = articleRepository.getById(id);
        if (article == null) {
            throw new ResourceNotFoundException("Article Not Found");
        }
        article.setStatus(ArticleStatus.PUBLISHED);
        article.setPublishedDate(LocalDateTime.now());
        article.setPublisherId(SpringSecurityUtil.getCurrentUser().getId());
        articleRepository.save(article);
        return true;
    }

    public List<ArticleShortInfoDTO> getArticlesByTypes(Integer articleTypeId) {
        List<String> stringList = articleTypeArticleService.getArticlesByTypes(articleTypeId);
        List<ArticleShortInfoMapper> mapperList = articleRepository.getByType(ArticleStatus.PUBLISHED, stringList,
                                                                               PageRequest.of(0, 5));

        List<ArticleShortInfoDTO> dtoList = new ArrayList<>();
        for (ArticleShortInfoMapper mapper : mapperList) {
            dtoList.add(changeInfoDTO(mapper));
        }

        return dtoList;
    }

    public List<ArticleShortInfoDTO> getLast8Articles(List<String> articleIds) {
        List<ArticleShortInfoMapper> mapperList = articleRepository.getLast8NotIn(articleIds, ArticleStatus.PUBLISHED,
                                                                                  PageRequest.of(0, 8));
       /* List<ArticleShortInfoDTO> infoDTOList = new ArrayList<>();
        for (ArticleShortInfoMapper entity : entityList) {
            infoDTOList.add(changeInfoDTO(entity));
        }
        return infoDTOList;*/

        return mapperList.stream().map(item -> changeInfoDTO(item)).toList();
    }

    public ArticleShortInfoDTO changeInfoDTO(ArticleShortInfoMapper mapper) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(mapper.getId());
        dto.setTitle(mapper.getTitle());
        dto.setDescription(mapper.getDescription());
        dto.setPhoto(attachService.getDto(mapper.getPhotoId()));
        dto.setPublishedDate(mapper.getPublishDate());
        return dto;
    }


    public ArticleDTO changeToDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setDescription(entity.getDescription());
        dto.setSharedCount(entity.getSharedCount());
        dto.setViewCount(entity.getViewCount());
        dto.setCategoryId(entity.getCategoryId());
        dto.setRegion(regionService.getById(entity.getRegionId()));
        dto.setPhoto(attachService.getDto(entity.getPhotoId()));
        dto.setPublishedDate(entity.getCreatedDate());
        return dto;
    }

    public List<ArticleShortInfoDTO> getLast4ExceptId(String articleId, Integer articleTypeId) {
        List<String> stringList = articleTypeArticleService.getArticlesByTypes(articleTypeId);
        if(stringList.contains(articleId)) {
            stringList.remove(articleId);
        }
        List<ArticleShortInfoMapper> mapperList = articleRepository.getByType(ArticleStatus.PUBLISHED,stringList,
                                                                              PageRequest.of(0, 4));
        return mapperList.stream().map(item -> changeInfoDTO(item)).toList();

    }

    public List<ArticleShortInfoDTO>  getMostRead() {
        List<ArticleShortInfoMapper> entityList = articleRepository.getMostRead(ArticleStatus.PUBLISHED,PageRequest.of(0,4));
        return entityList.stream().map(item -> changeInfoDTO(item)).toList();
    }


    public List<ArticleShortInfoDTO> getByTage(String tage) {
        List<ArticleShortInfoMapper> mapperList = articleRepository.getByTage(tage,ArticleStatus.PUBLISHED);
        return mapperList.stream().map(item -> changeInfoDTO(item)).toList();
    }

    public List<ArticleShortInfoDTO> getByRegion(String regionId) {
        Page<ArticleShortInfoMapper> mapperList = articleRepository.getByRegion(ArticleStatus.PUBLISHED,regionId,
                                                                                 PageRequest.of(0,4));
        return mapperList.stream().map(item -> changeInfoDTO(item)).toList();
    }

    public Page<ArticleShortInfoDTO> getByPagRegion(String regionId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleShortInfoMapper> mapperList = articleRepository.getByRegion(ArticleStatus.PUBLISHED,regionId,pageable);
        List<ArticleShortInfoDTO> dtoList = new ArrayList<>();
        for(ArticleShortInfoMapper mapper : mapperList) {
            dtoList.add(changeInfoDTO(mapper));
        }

        return new PageImpl<>(dtoList,pageable,mapperList.getTotalPages());
    }

    public Page<ArticleShortInfoDTO> getByPagCategory(Integer categoryId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleShortInfoMapper> mapperList = articleRepository.getByCategory(ArticleStatus.PUBLISHED,categoryId,pageable);
        List<ArticleShortInfoDTO> dtoList = new ArrayList<>();
        for(ArticleShortInfoMapper mapper : mapperList) {
            dtoList.add(changeInfoDTO(mapper));
        }
        return new PageImpl<>(dtoList,pageable,mapperList.getTotalPages());
    }

    public  List<ArticleShortInfoDTO>  getByCategory(Integer categoryId) {
        Page<ArticleShortInfoMapper> mapperList = articleRepository.getByCategory(ArticleStatus.PUBLISHED,categoryId,PageRequest.of(0,5));
        List<ArticleShortInfoDTO> dtoList = new ArrayList<>();
        for(ArticleShortInfoMapper mapper : mapperList) {
            dtoList.add(changeInfoDTO(mapper));
        }
        return dtoList;
    }


    public ArticleShortInfoDTO byId(String articleId, String ip) {
        ArticleEntity entity = articleRepository.getById(articleId);
        if(!ipArticleService.isExistIp(ip, articleId)){
            entity.setViewCount(entity.getViewCount()+1);
            articleRepository.save(entity);
        }
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPhoto(attachService.getDto(entity.getPhotoId()));
        dto.setPublishedDate(entity.getPublishedDate());

        return dto;
    }

    public void sharedCount(String articleId) {
        articleRepository.sharedCount(articleId);
    }


    public void like(String articleId, Integer profileId, LikeStatus status) {
       articleLikeService.create(articleId,profileId,status);
    }
}
