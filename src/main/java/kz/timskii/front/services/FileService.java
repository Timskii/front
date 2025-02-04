package kz.timskii.front.services;


import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class FileService {
    private static final String UPLOAD_DIR = "uploads/";

    public void upload (InputStream inputStream, String fileName){
        // Путь для сохранения файла
        File targetFile = new File(UPLOAD_DIR + fileName);

        // Убедимся, что папка "uploads" существует
        targetFile.getParentFile().mkdirs();

        // Сохранение данных в файл
        try (OutputStream outputStream = new FileOutputStream(targetFile)) {
            inputStream.transferTo(outputStream);
            Notification.show("File saved: " + targetFile.getAbsolutePath());
        } catch (IOException e) {
            Notification.show("Error saving file: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            e.printStackTrace();
        }
    }

    public Map<String, String> getFilenamesByFolder(String folderName){
        Map<String, String > imageUrls = new HashMap<>();
        final File folder = new File(UPLOAD_DIR+folderName);


        // Проверяем наличие папки и файлов
        if (folder.exists() && folder.isDirectory()) {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile() && isImage(fileEntry.getName())) {
                    // Генерируем URL для доступа к изображению
                    imageUrls.put(fileEntry.getName(), "/images?filename=" +fileEntry.getName() + "&folder=" + folderName);
                }
            }
        }
        return imageUrls;
    }

    private boolean isImage(String fileName) {
        // Проверяем расширение файла
        String lowerName = fileName.toLowerCase();
        return lowerName.endsWith(".png") || lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") || lowerName.endsWith(".gif");
    }

    public List<String> getFolders(String folderName){
        List<String> folders = new ArrayList<>();

        final File folder = new File(folderName);
        if (folder.listFiles().length >0) {
            Arrays.stream(folder.listFiles()).filter(f -> f.isDirectory()).forEach(f-> folders.add(f.getName()));
        }

        return folders;
    }
}
