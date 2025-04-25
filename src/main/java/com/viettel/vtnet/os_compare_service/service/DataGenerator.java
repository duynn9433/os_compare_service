package com.viettel.vtnet.os_compare_service.service;

import java.io.FileWriter;
import java.io.IOException;

public class DataGenerator {
    public static void main(String[] args) throws IOException {
        FileWriter writer = new FileWriter("input.txt");
        for (int i = 1; i <= 10000; i++) {
            writer.write("Dòng số " + i + "\n");
        }
        writer.close();
        System.out.println("Đã tạo file input.txt với 10.000 dòng.");
    }
}