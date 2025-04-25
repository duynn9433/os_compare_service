package com.viettel.vtnet.os_compare_service.entity.mariadb;

import com.github.javafaker.Faker;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Random;

@Data
@Entity
public class Entity2 {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public static Entity2 generateRandom() {
        Faker faker = new Faker();
        Random rand = new Random();
        Entity2 e = new Entity2();
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
        return e;
    }

}
