package kun.uz.controller;

import kun.uz.service.EmailHistoryService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/email-history")
public class EmailHistoryController {

    @Autowired
    private EmailHistoryService emailHistoryService;

    @GetMapping("/email")
    public ResponseEntity<?> getHistoryByEmail(@RequestParam String email) {
        return ResponseEntity.ok(emailHistoryService.getHistoryByEmail(email));
    }

    @GetMapping("/date")
    public ResponseEntity<?> getHistoryByGivenDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(emailHistoryService.getHistoryByGivenDate(date));

    }

    @GetMapping("/pagination")
    public ResponseEntity<?> getPagination(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(emailHistoryService.getHistoryByPagination((page-1), size));
    }
}
