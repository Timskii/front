package kz.timskii.front.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import kz.timskii.front.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

@Route("upload-basic")
@PermitAll

public class UploadBasic extends Div {

    @Autowired
    private FileService fileService;

    public UploadBasic() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();

            fileService.upload(buffer.getInputStream(fileName), fileName);
        });


        add(upload);
    }

}
