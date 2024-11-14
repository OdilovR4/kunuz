package kun.uz.service;

import kun.uz.dto.article.ArticleDTO;
import kun.uz.dto.article.CommentDTO;
import kun.uz.dto.filter.CommentFilterDTO;
import kun.uz.dto.filter.FilterResultDTO;
import kun.uz.dto.profile.ProfileDTO;
import kun.uz.entity.CommentEntity;
import kun.uz.exceptions.ResourceNotFoundException;
import kun.uz.repository.CommentRepository;
import kun.uz.repository.CustomCommentRepository;
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
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CustomCommentRepository customCommentRepository;

    public CommentDTO create(CommentDTO comment) {
        CommentEntity entity = new CommentEntity();
        if (comment.getReplyId() != null) {
            entity.setReplyId(comment.getReplyId());
        }
        entity.setContent(comment.getContent());
        entity.setProfileId(SpringSecurityUtil.getCurrentUser().getId());
        entity.setArticleId(comment.getArticleId());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(Boolean.TRUE);
        commentRepository.save(entity);
        comment.setProfileId(entity.getProfileId());
        comment.setReplyId(entity.getReplyId());
        return comment;
    }

    public CommentEntity getById(String id) {
        return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }

    public CommentDTO update(CommentDTO comment) {
        CommentEntity commentEntity = getById(comment.getId());
        commentEntity.setContent(comment.getContent());
        commentEntity.setUpdateDate(LocalDateTime.now());
        commentRepository.save(commentEntity);
        return changeToDTO(commentEntity);
    }

    public CommentDTO changeToDTO(CommentEntity entity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent(entity.getContent());
        commentDTO.setArticleId(entity.getArticleId());
        commentDTO.setProfileId(entity.getProfileId());
        commentDTO.setReplyId(entity.getReplyId());
        return commentDTO;

    }

    public Boolean delete(String commentId) {
        return commentRepository.deleteArticle(commentId)==1;
    }

    public List<CommentDTO> getByArticleId(String articleId) {
        List<CommentEntity> commentEntities = commentRepository.getBYArticleId(articleId);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities) {
            commentDTOS.add(changeToGet(commentEntity));
        }
        return commentDTOS;
    }
    public CommentDTO changeToGet(CommentEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getProfile().getName());
        dto.setId(entity.getProfileId());
        dto.setSurname(entity.getProfile().getSurname());
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setProfileDTO(dto);
        commentDTO.setCreatedDate(entity.getCreatedDate());
        commentDTO.setUpdatedDate(entity.getUpdateDate());
        return commentDTO;
    }

    public Page<CommentDTO> getByPage(String articleId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<CommentEntity> commentEntityPage = commentRepository.getByPage(articleId,pageable);
        List<CommentDTO> dtoList = commentEntityPage.stream().map(item -> changeToPage(item)).toList();
        return new PageImpl<>(dtoList, pageable, commentEntityPage.getTotalElements());
    }
    public CommentDTO changeToPage(CommentEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getProfile().getName());
        dto.setId(entity.getProfileId());
        dto.setSurname(entity.getProfile().getSurname());

        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(entity.getArticleId());
        articleDTO.setTitle(entity.getArticle().getTitle());
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setProfileDTO(dto);
        commentDTO.setArticleDTO(articleDTO);
        commentDTO.setContent(entity.getContent());
        commentDTO.setCreatedDate(entity.getCreatedDate());
        commentDTO.setUpdatedDate(entity.getUpdateDate());
        commentDTO.setReplyId(entity.getReplyId());
        return commentDTO;
    }

    public Page<CommentDTO> filter(Integer page, Integer size, CommentFilterDTO filter) {
        FilterResultDTO<CommentEntity> result = customCommentRepository.filter(page,size,filter);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for(CommentEntity entity : result.getContents()){
            CommentDTO dto = (changeToDTO(entity));
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setUpdatedDate(entity.getUpdateDate());
            dto.setId(entity.getId());
            commentDTOS.add(dto);
        }
        return new PageImpl<>(commentDTOS,PageRequest.of(page,size),result.getTotal());
    }
}
