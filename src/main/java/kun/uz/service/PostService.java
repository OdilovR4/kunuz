package kun.uz.service;

import kun.uz.dto.post.PostDTO;
import kun.uz.entity.PostAttachEntity;
import kun.uz.entity.PostEntity;
import kun.uz.exceptions.ResourceNotFoundException;
import kun.uz.repository.PostAttachRepository;
import kun.uz.repository.PostRepository;
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
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostAttachRepository postAttachRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;


    public PostDTO create(PostDTO postDTO) {
        PostEntity entity = new PostEntity();
        entity.setTitle(postDTO.getTitle());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(Boolean.TRUE);
        postRepository.save(entity);
        createToPostAttach(entity.getId(), postDTO.getImagesId());
        return postDTO;
    }

    private void createToPostAttach(Integer postId, List<String> imagesId) {
        for (String imageId : imagesId) {
            PostAttachEntity entity = new PostAttachEntity();
            entity.setPostId(postId);
            entity.setAttachId(imageId);
            entity.setCreatedDate(LocalDateTime.now());
            postAttachRepository.save(entity);
        }
    }

    public PageImpl getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postEntityPage = postRepository.getAllAndVisibleTrue(pageable);
        List<PostDTO> dtoList = new ArrayList<>();
        for (PostEntity postEntity : postEntityPage) {
            List<String> photos = getPhotos(postEntity.getId());
            dtoList.add(getDto(postEntity, photos));
        }
        return new PageImpl(dtoList, pageable, postEntityPage.getTotalElements());
    }

    public List<String> getPhotos(Integer postId) {
        return postAttachRepository.getPhotosById(postId);
    }

    public PostDTO getDto(PostEntity entity, List<String> imagesId) {
        PostDTO dto = new PostDTO();
        dto.setTitle(entity.getTitle());
        dto.setImagesId(imagesId);
        return dto;
    }

    public Boolean delete(Integer postId) {
        return postRepository.deletePost(postId) == 1;

    }

    public Boolean update(Integer id, PostDTO postDTO,String lang) {
        PostEntity entity = getById(id,lang);
        entity.setTitle(postDTO.getTitle());
        updatePhotos(entity.getId(), postDTO.getImagesId());
        return true;
    }

    public PostEntity getById(Integer id, String lang) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                resourceBundleService.getMessage("post.not.found",lang)));
    }

    private void updatePhotos(Integer postId, List<String> imagesId) {
        postAttachRepository.deleteAllPhotos(postId);
        createToPostAttach(postId, imagesId);
    }
}
