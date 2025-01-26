package kz.timskii.front.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.io.*;

@Route("upload-basic")
@PermitAll
public class UploadBasic extends Div {

    public UploadBasic() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            // Путь для сохранения файла
            File targetFile = new File("uploads/" + fileName);

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

            // Do something with the file data
            // processFile(inputStream, fileName);
        });


        add(upload);
    }

}
