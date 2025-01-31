package kun.uz.service;

import jakarta.validation.Valid;
import kun.uz.dto.filter.FilterDTO;
import kun.uz.dto.filter.FilterResultDTO;
import kun.uz.dto.profile.ProfileCreationDTO;
import kun.uz.dto.profile.ProfileDTO;
import kun.uz.dto.profile.UpdateProfileDetail;
import kun.uz.entity.ProfileEntity;
import kun.uz.enums.ProfileStatus;
import kun.uz.exceptions.ResourceNotFoundException;
import kun.uz.repository.CustomProfileRepository;
import kun.uz.repository.ProfileRepository;
import kun.uz.util.BCryptUtil;
import kun.uz.util.MD5Util;
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
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;

    @Autowired
    private CustomProfileRepository profileCustomRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;

    public ProfileDTO create(ProfileCreationDTO dto, String lang) {
        ProfileEntity entity = profileRepository.getByUsername(dto.getUsername());
        if (entity != null) {
            throw new ResourceNotFoundException(resourceBundleService.getMessage("username.in.use",lang));
        }
        ProfileEntity profile = new ProfileEntity();
        profile.setUsername(dto.getUsername());
        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
       // profile.setPassword(MD5Util.md5(dto.getPassword()));
        profile.setPassword(MD5Util.md5(dto.getPassword()));
        profile.setStatus(ProfileStatus.ACTIVE);
        profile.setVisible(Boolean.TRUE);
        profile.setRole(dto.getRole());
        profile.setCreatedDate(LocalDateTime.now());
        profileRepository.save(profile);

        return changeToDto(profile);


    }

//    public static void jwtValidator(String jwtToken) {
//        JwtDTO dto = JwtUtil.decode(jwtToken);
//        if (dto.getUsername() == null || dto.getRole() == null) {
//            throw new AppBadRequestException("Invalid JWT to do smth ");
//        }
//        if (!dto.getRole().equals(ProfileRole.ROLE_ADMIN.name())) {
//            throw new AppBadRequestException("Invalid role to do smth");
//        }
//    }

    public boolean updateByAdmin(Integer id, @Valid ProfileCreationDTO profile, String lang) {
        ProfileEntity entity = getById(id, lang);
        entity.setName(profile.getName());
        entity.setUsername(profile.getUsername());
        entity.setSurname(profile.getSurname());
        entity.setPassword(profile.getPassword());
        entity.setRole(profile.getRole());
        profileRepository.save(entity);
        return true;
    }

    public Page<ProfileDTO> getAll(int page, int size) {
        Pageable pagination = PageRequest.of(page, size);
        Page<ProfileEntity> entityList = profileRepository.findAllByVisibleTrue(pagination);
        List<ProfileDTO> dtoList = new ArrayList<>();
        for (ProfileEntity entity : entityList) {
            dtoList.add(changeToDto(entity));
        }
        return new PageImpl<>(dtoList, pagination, entityList.getTotalElements());
    }

    public ProfileEntity getById(Integer id, String lang) {
        return profileRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(resourceBundleService.getMessage("user.not.found",lang)));

    }

    public ProfileDTO changeToDto(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername(entity.getUsername());
        dto.setName(entity.getName());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setSurname(entity.getSurname());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPhoto(attachService.getDto(entity.getPhotoId()));

        return dto;
    }

    public Boolean delete(Integer id) {
        return (profileRepository.deleteProfile(id) > 0);
    }

    public Page<ProfileDTO> filter(int page, Integer size, FilterDTO dto) {
        FilterResultDTO<ProfileEntity> result = profileCustomRepository.filter(page, size, dto);
        List<ProfileDTO> dtoList = new ArrayList<>();
        for (ProfileEntity entity : result.getContents()) {
            dtoList.add(changeToDto(entity));
        }
        return new PageImpl<>(dtoList, PageRequest.of(page, size), result.getTotal());
    }

    public Boolean updateByOwn(@Valid UpdateProfileDetail dto, String username, String lang) {
        ProfileEntity entity = getByUsername(username,lang);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        if(entity.getPhotoId()!=null){
            attachService.delete(entity.getPhotoId(),lang);
        }
        entity.setPhotoId(dto.getPhotoId());
        profileRepository.save(entity);
        return true;
    }

    public ProfileEntity getByUsername(String username, String lang) {
        ProfileEntity entity = profileRepository.findByUsername(username);
        if (entity == null) {
            throw new ResourceNotFoundException(resourceBundleService.getMessage("user.not.found",lang));
        }
        return entity;
    }

    public ProfileDTO getProfile(Integer profileId, String lang) {
        ProfileEntity entity = getById(profileId,lang);
        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        return dto;
    }
}
