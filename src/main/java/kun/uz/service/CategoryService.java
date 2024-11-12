package kun.uz.service;

import kun.uz.dto.category.CategoryDTO;
import kun.uz.dto.base.NameOrder;
import kun.uz.dto.region.RegionDTO;
import kun.uz.entity.CategoryEntity;
import kun.uz.exceptions.ResourceNotFoundException;
import kun.uz.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setVisible(true);
        entity.setNameUz(dto.getNameUz());
        categoryRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public String updateCategory(Integer id, CategoryDTO dto) {
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Not found"));
        if(dto.getOrderNumber()!=null){
            entity.setOrderNumber(dto.getOrderNumber());
        }
        if(dto.getNameUz()!=null){
            entity.setNameUz(dto.getNameUz());
        }
        if(dto.getNameRu()!=null){
            entity.setNameRu(dto.getNameRu());
        }
        if(dto.getNameEn()!=null){
            entity.setNameEn(dto.getNameEn());
        }

        categoryRepository.save(entity);

        return "Updated";
    }

    public String delete(Integer id) {
        if(categoryRepository.deleteCategory(id)==1) {
            return "Deleted";
        }
        return "Not Deleted";
    }

    public List<CategoryDTO> getAll() {
        return changerToDTOList(categoryRepository.findAllCategories());
    }

    public List<NameOrder> getByLang(String lang) {
        return categoryRepository.getByLang(lang);
    }

    public List<CategoryDTO> changerToDTOList(List<CategoryEntity> entities) {
        List<CategoryDTO> dtos = new ArrayList<>();
        for (CategoryEntity entity : entities) {
            dtos.add(changeToDTO(entity));
        }
        return dtos;
    }

    public CategoryDTO changeToDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setNameUz(entity.getNameUz());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public CategoryDTO getById(Integer categoryID) {
        return changeToDTO(categoryRepository.findById(categoryID).orElseThrow(() -> new ResourceNotFoundException("Not found")));
    }
}
