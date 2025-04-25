package com.viettel.vtnet.os_compare_service.controller;

import com.viettel.vtnet.os_compare_service.service.FibonacciService;
import com.viettel.vtnet.os_compare_service.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pb")
public class ProcessBackendController {
    private final FibonacciService fibonacciService;
    private final FileService fileService;

    public ProcessBackendController(FibonacciService fibonacciService, FileService fileService) {
        this.fibonacciService = fibonacciService;
        this.fileService = fileService;
    }

    @GetMapping("/fib")
    public ResponseEntity<?> fib(@RequestParam int n, @RequestParam int m) {
        long res = 0;
        for (int i = 0; i < m; i++) {
           res =  fibonacciService.fibonacci(n);
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/read-file")
    public ResponseEntity<?> readFile() {
        String resp = fileService.readFile("input.txt");
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/write-file")
    public ResponseEntity<?> writeFile() {
        String resp = fileService.writeFile("output.txt");
        return ResponseEntity.ok(resp);
    }
}
