package com.viettel.vtnet.os_compare_service.entity.postgres;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PlusEntity {
    @Id
    private long id;
    private int a;
    private int b;

    public long getResult() {
        return a + b;
    }

    public void setIdInit(long id) {
        this.id = id;
    }

    public static PlusEntity generateRandom() {
        PlusEntity plusEntity = new PlusEntity();
        plusEntity.a = (int) (Math.random() * 100);
        plusEntity.b = (int) (Math.random() * 100);
        return plusEntity;
    }
}
