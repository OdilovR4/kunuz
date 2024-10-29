package kun.uz.service;

import jakarta.validation.Valid;
import kun.uz.dto.*;
import kun.uz.entity.ProfileEntity;
import kun.uz.enums.ProfileRole;
import kun.uz.enums.ProfileStatus;
import kun.uz.exceptions.AppBadRequestException;
import kun.uz.exceptions.ResourceNotFoundException;
import kun.uz.repository.CustomProfileRepository;
import kun.uz.repository.ProfileRepository;
import kun.uz.util.JwtUtil;
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
    ProfileRepository profileRepository;

    @Autowired
    CustomProfileRepository profileCustomRepository;

    public ProfileDTO create(ProfileCreationDTO dto, String jwtToken) {
        ProfileEntity entity = profileRepository.getByUsername(dto.getUsername());
        if(entity != null) {
         throw new ResourceNotFoundException("Profile already exist");
        }
        jwtValidator(jwtToken);
        ProfileEntity profile = new ProfileEntity();
        profile.setUsername(dto.getUsername());
        profile.setPassword(dto.getPassword());
        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        profile.setPassword(MD5Util.md5(dto.getPassword()));
        profile.setStatus(ProfileStatus.ACTIVE);
        profile.setVisible(Boolean.TRUE);
        profile.setRole(dto.getRole());
        profile.setCreatedDate(LocalDateTime.now());
        profileRepository.save(profile);

        return changeToDto(profile);


    }

    private void jwtValidator(String jwtToken) {
        JwtDTO dto = JwtUtil.decode(jwtToken);
        if(dto.getUsername()==null || dto.getRole() == null) {
            throw new AppBadRequestException("Invalid JWT to create profile");
        }
        if(!dto.getRole().equals(ProfileRole.ROLE_ADMIN.name())){
            throw new AppBadRequestException("Invalid role to create profile");
        }
    }



    public boolean update(Integer id, @Valid ProfileDTO profile) {
        ProfileEntity entity = getById(id);
        entity.setName(profile.getName());
        entity.setUsername(profile.getUsername());
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
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setSurname(entity.getSurname());
        dto.setPhotoId(entity.getPhotoId());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());

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
