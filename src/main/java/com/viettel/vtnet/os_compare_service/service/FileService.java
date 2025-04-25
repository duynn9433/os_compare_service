package com.viettel.vtnet.os_compare_service.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Log4j2
public class FileService {
    @Value("${file.lineCount}")
    private int lineCount;


    public String writeFile(String filePath) {
        File file = new File(filePath);

        // Nếu file đã tồn tại, xóa file cũ
        if (file.exists()) {
            if (file.delete()) {
                log.info("File cũ đã bị xóa.");
            } else {
                return "Không thể xóa file cũ.";
            }
        }

        // Ghi dữ liệu vào file mới
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 1; i <= lineCount; i++) {
                writer.write("Dòng số " + i + "\n");
            }
            return "Đã ghi " + lineCount + " dòng vào file: " + filePath;
        } catch (IOException e) {
            return "Lỗi khi ghi file: " + e.getMessage();
        }
    }
    public String readFile(String filePath) {
        int lineCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
            return "Đọc thành công file: " + filePath + " với tổng số dòng: " + lineCount;
        } catch (IOException e) {
            return "Lỗi khi đọc file: " + e.getMessage();
        }
    }
}
