package com.viettel.vtnet.os_compare_service.service;

import com.viettel.vtnet.os_compare_service.entity.postgres.PlusEntity;
import com.viettel.vtnet.os_compare_service.repo.postgres.PlusRepository;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    private final PlusRepository plusRepository;

    public CalculatorService(PlusRepository plusRepository) {
        this.plusRepository = plusRepository;
    }

    public long add(int a, int b) {
        return a + b;
    }

    public long plusFromDb(long id) {
        PlusEntity plusEntity = findPlusEntityById(id);
        return plusEntity.getResult();
    }

    public PlusEntity findPlusEntityById(long id) {
        return plusRepository.findById(id).orElseThrow();
    }


}
