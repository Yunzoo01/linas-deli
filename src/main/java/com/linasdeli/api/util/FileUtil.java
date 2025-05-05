package com.linasdeli.api.util;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FileUtil {

    private final String BASE_UPLOAD_PATH = "uploads/";

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(BASE_UPLOAD_PATH));
        } catch (IOException e) {
            throw new RuntimeException("업로드 디렉토리 생성 실패: " + e.getMessage());
        }
    }

    public UploadResult saveImage(MultipartFile file, String folderName) {
        if (file == null || file.isEmpty()) return null;

        try {
            String uniqueName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                    + "_" + file.getOriginalFilename();

            Path uploadPath = Paths.get(BASE_UPLOAD_PATH + folderName);
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(uniqueName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String url = "/uploads/" + folderName + "/" + uniqueName;
            return new UploadResult(uniqueName, url);

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패: " + e.getMessage());
        }
    }

    // ✅ UploadResult는 내부 클래스로 둬도 되고, 별도 클래스로 빼도 됨
    public static class UploadResult {
        private final String fileName;
        private final String url;

        public UploadResult(String fileName, String url) {
            this.fileName = fileName;
            this.url = url;
        }

        public String getFileName() { return fileName; }
        public String getUrl() { return url; }
    }
}
