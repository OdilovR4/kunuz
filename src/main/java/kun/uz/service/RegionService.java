package kun.uz.service;

import kun.uz.dto.base.NameInterface;
import kun.uz.dto.region.RegionDTO;
import kun.uz.entity.RegionEntity;
import kun.uz.exceptions.ResourceNotFoundException;
import kun.uz.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionDTO createRegion(RegionDTO dto) {
        RegionEntity entity = new RegionEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setNameUz(dto.getNameUz());
        entity.setVisible(true);
        regionRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    public String updateRegion(Integer id, RegionDTO dto) {
        RegionEntity entity = regionRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Not found"));
        if(dto.getOrderNumber() != null) {
            entity.setOrderNumber(dto.getOrderNumber());
        }
        if(dto.getNameEn() != null) {
            entity.setNameEn(dto.getNameEn());
        }
        if(dto.getNameRu() != null) {
            entity.setNameRu(dto.getNameRu());
        }
        if(dto.getNameUz() != null) {
            entity.setNameUz(dto.getNameUz());
        }
         regionRepository.save(entity);

        return "Updated";
    }

    public String delete(Integer id) {
        if(regionRepository.deleteRegion(id)==1){
            return "Deleted";
        }
        return "Not Deleted";
    }

    public List<RegionDTO> getAll() {
       /* List<RegionEntity> entityList = regionRepository.allRegions();
        List<RegionDTO> dtoList = new ArrayList<>();
        for(RegionEntity e : entityList){
            RegionDTO dto = new RegionDTO();
            dto.setId(e.getId());
            dto.setOrderNumber(e.getOrderNumber());
            dto.setNameEn(e.getNameEn());
            dto.setNameRu(e.getNameRu());
            dto.setNameUz(e.getNameUz());
            dto.setVisible(e.getVisible());
            dto.setCreatedDate(e.getCreatedDate());
            dtoList.add(dto);
        }
        return dtoList;*/
        return changeToDTOList(regionRepository.allRegions());
    }

    public List<RegionDTO> changeToDTOList(List<RegionEntity> entityList) {
        List<RegionDTO> dtoList = new ArrayList<>();
        for(RegionEntity e : entityList){
            dtoList.add(changeToDTO(e));
        }
        return dtoList;
    }

    public RegionDTO changeToDTO(RegionEntity e) {
        RegionDTO dto = new RegionDTO();
        dto.setId(e.getId());
        dto.setOrderNumber(e.getOrderNumber());
        dto.setNameEn(e.getNameEn());
        dto.setNameRu(e.getNameRu());
        dto.setNameUz(e.getNameUz());
        dto.setCreatedDate(e.getCreatedDate());
        return dto;
    }

    public List<NameInterface> getByLang(String lang) {
        return regionRepository.getByLang(lang);

    }

    public RegionDTO getById(Integer regionId) {
        return changeToDTO(regionRepository.findById(regionId).orElseThrow(() -> new ResourceNotFoundException("Not found")));
    }
}
