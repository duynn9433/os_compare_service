package com.viettel.vtnet.os_compare_service.entity.mariadb;

import com.github.javafaker.Faker;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Random;

@Data
@Entity
public class Entity42 {
    @Id
    long id;
    private boolean att1;
    private byte att2;
    private short att3;
    private int att4;
    private long att5;
    private float att6;
    private double att7;
    private char att8;
    private String att9;
    private String att10;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Entity41 att11;

    public static Entity42 generateRandom() {
        Faker faker = new Faker();
        Random rand = new Random();
        Entity42 e = new Entity42();
        e.setAtt1(rand.nextBoolean());
        e.setAtt2((byte) rand.nextInt(128));
        e.setAtt3((short) rand.nextInt(Short.MAX_VALUE));
        e.setAtt4(rand.nextInt());
        e.setAtt5(rand.nextLong());
        e.setAtt6(rand.nextFloat());
        e.setAtt7(rand.nextDouble());
        e.setAtt8((char) ('A' + rand.nextInt(26))); // Random letter A-Z
        e.setAtt9(faker.name().fullName()); // Random name
        e.setAtt10(faker.lorem().sentence()); // Random sentence
        e.setAtt11(Entity41.generateRandom());
        return e;
    }

    public void setIdInit(long id) {
        this.id = id;
        if (att11 != null) {
            att11.setIdInit(id);
        } else {
            att11 = Entity41.generateRandom();
            att11.setIdInit(id);
        }
    }

}
