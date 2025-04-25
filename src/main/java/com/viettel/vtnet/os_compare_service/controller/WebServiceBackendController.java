package com.viettel.vtnet.os_compare_service.controller;

import com.viettel.vtnet.os_compare_service.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/wsb")
public class WebServiceBackendController {
    private final CalculatorService calculatorService;

    public WebServiceBackendController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/plus")
    public ResponseEntity<?> plus(@RequestParam int a, @RequestParam int b) {
        return ResponseEntity.ok(calculatorService.add(a, b));
    }

    @GetMapping("/plus/{id}")
    public ResponseEntity<?> plus(@PathVariable long id) {
        return ResponseEntity.ok(calculatorService.plusFromDb(id));
    }

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        // Có thể lưu vào ổ đĩa tại đây nếu cần, ví dụ:
        // Files.copy(file.getInputStream(), Paths.get("uploads/" + file.getOriginalFilename()));

        String fileName = file.getOriginalFilename();
        long size = file.getSize();

        return ResponseEntity.ok("Received file: " + fileName + " (" + size + " bytes)");
    }
}
