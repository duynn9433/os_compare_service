package com.viettel.vtnet.os_compare_service.service;

import org.springframework.stereotype.Service;

@Service
public class FibonacciService {
    public long fibonacci(int n) {
        return FibonacciMatrix.fib(n);
    }
}
