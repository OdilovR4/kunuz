package kun.uz.service;

import jakarta.validation.Valid;
import kun.uz.dto.FilterDTO;
import kun.uz.dto.FilterResultDTO;
import kun.uz.dto.ProfileDTO;
import kun.uz.entity.ProfileEntity;
import kun.uz.enums.ProfileStatus;
import kun.uz.exceptions.ResourceNotFoundException;
import kun.uz.repository.CustomProfileRepository;
import kun.uz.repository.ProfileRepository;
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
    ProfileRepository profileRepository;

    @Autowired
    CustomProfileRepository profileCustomRepository;

    public ProfileDTO create(ProfileDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setSurname(dto.getSurname());
        entity.setPhotoId(dto.getPhotoId());
        entity.setVisible(true);
        profileRepository.save(entity);
        return dto;
    }


    public boolean update(Integer id, @Valid ProfileDTO profile) {
        ProfileEntity entity = getById(id);
        entity.setName(profile.getName());
        entity.setEmail(profile.getEmail());
        entity.setPassword(profile.getPassword());
        entity.setRole(profile.getRole());
        entity.setSurname(profile.getSurname());
        entity.setPhotoId(profile.getPhotoId());
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

    public ProfileEntity getById(Integer id) {
        return profileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

    }

    public ProfileDTO changeToDto(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setSurname(entity.getSurname());
        dto.setPhotoId(entity.getPhotoId());
        return dto;
    }

    public Boolean delete(Integer id) {

        return (profileRepository.deleteProfile(id) > 0);
    }

    public Page<ProfileDTO> filter(int page, Integer size, FilterDTO dto){
        FilterResultDTO<ProfileEntity> result = profileCustomRepository.filter(page, size, dto);
        List<ProfileDTO> dtoList = new ArrayList<>();
        for (ProfileEntity entity : result.getContents()) {
            dtoList.add(changeToDto(entity));
        }
        return new PageImpl<>(dtoList, PageRequest.of(page, size), result.getTotal());
    }

}
