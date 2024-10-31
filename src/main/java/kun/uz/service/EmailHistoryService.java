package kun.uz.service;

import kun.uz.dto.profile.EmailHistoryDTO;
import kun.uz.entity.EmailHistoryEntity;
import kun.uz.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    public void addEmailHistory(EmailHistoryDTO dto){
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setEmail(dto.getEmail());
        entity.setMessage(dto.getMessage());
        entity.setCreatedDate(dto.getCreatedDate());
        emailHistoryRepository.save(entity);
    }

    public List<EmailHistoryDTO> getHistoryByEmail(String email){
        List<EmailHistoryEntity> entityList = emailHistoryRepository.findAllByEmail(email);
        List<EmailHistoryDTO> dtoList = new ArrayList<>();
        for(EmailHistoryEntity entity : entityList){
            dtoList.add(changeToDTO(entity));
        }
        return dtoList;
    }

    public List<EmailHistoryDTO> getHistoryByGivenDate(LocalDate date){
        LocalDateTime from = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime to = LocalDateTime.of(date, LocalTime.MAX);
        List<EmailHistoryEntity> entityList = emailHistoryRepository.getByGivenDate(from,to);
        List<EmailHistoryDTO> dtoList = new ArrayList<>();
        for(EmailHistoryEntity entity : entityList){
            dtoList.add(changeToDTO(entity));
        }
        return dtoList;
    }

    public PageImpl<EmailHistoryDTO> getHistoryByPagination(Integer page, Integer size){
     Pageable pageable = PageRequest.of(page, size);
     Page<EmailHistoryEntity> pageList = emailHistoryRepository.getByPagination(pageable);
     List<EmailHistoryDTO> dtoList = new ArrayList<>();
     for(EmailHistoryEntity entity : pageList.getContent()){
         dtoList.add(changeToDTO(entity));
     }
     return new PageImpl<>(dtoList, pageable, pageList.getTotalElements());
    }

    private EmailHistoryDTO changeToDTO(EmailHistoryEntity entity) {
        EmailHistoryDTO dto = new EmailHistoryDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setMessage(entity.getMessage());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
