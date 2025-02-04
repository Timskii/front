package kz.timskii.front.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/images")
    public ResponseEntity<Resource> getImage(@RequestParam String filename, @RequestParam String folder) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR + folder).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Определяем MIME-тип файла
                String mimeType = Files.probeContentType(filePath);
                if (mimeType == null) {
                    mimeType = "application/octet-stream"; // Устанавливаем тип по умолчанию
                }

                // Устанавливаем заголовки
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                        .contentType(MediaType.parseMediaType(mimeType)) // Устанавливаем MIME-тип
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
