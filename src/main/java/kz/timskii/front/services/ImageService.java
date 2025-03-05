package kz.timskii.front.services;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

import static kz.timskii.front.utils.Constants.UPLOAD_DIR;

public class ImageService {
    public static String generateThumbnail(String originalPath, String thumbnailPath, String foldername, String filename) {
        try {
            File thumbnailDir = new File(UPLOAD_DIR+"/"+foldername+"/thumbnails");
            File thumbnailFile = new File(thumbnailPath);
            if (!thumbnailDir.exists()) {
                thumbnailDir.mkdirs(); // Создаём все вложенные папки
            }
            if (!thumbnailFile.exists()) {
                Thumbnails.of(originalPath)
                        .size(500, 500)  // Размер превью
                        .toFile(thumbnailPath);
            }
            return "/images?filename=" + filename + "&folder=" + foldername+"/thumbnails";
        } catch (IOException e) {
            e.printStackTrace();
            return originalPath;  // В случае ошибки возвращаем оригинал
        }
    }
}
