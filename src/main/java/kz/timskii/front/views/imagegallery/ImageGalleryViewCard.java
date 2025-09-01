package kz.timskii.front.views.imagegallery;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import kz.timskii.front.services.ImageService;

import java.io.File;


public class ImageGalleryViewCard extends ListItem {

    public ImageGalleryViewCard(String filename, String url, String foldername) {

        String thumbnailPath = "uploads/"+ foldername + "/thumbnails/" + filename;
        String originalPath = "uploads/" + foldername +"/"+ filename;

        addClassNames(Background.CONTRAST_5, Display.FLEX, FlexDirection.COLUMN, AlignItems.START, Padding.MEDIUM,
                BorderRadius.LARGE);

        Div div = new Div();
        div.addClassNames(Background.CONTRAST, Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER,
                Margin.Bottom.MEDIUM, Overflow.HIDDEN, BorderRadius.MEDIUM, Width.FULL);
        div.setHeight("200px");

        // Генерируем превью, если его ещё нет
        String thumbnailUrl = ImageService.generateThumbnail(originalPath, thumbnailPath, foldername, filename);


        Image image = new Image(thumbnailUrl, filename);

        Dialog preview = new Dialog();
        preview.setSizeFull();


        image.addClickListener( e -> {
            preview.open();
        } );
        image.setWidth("100%");
//        image.setSrc(url);
//        image.setAlt(filename);

        div.add(image);

//        Span header = new Span();
//        header.addClassNames(FontSize.XLARGE, FontWeight.SEMIBOLD);
//        header.setText("Title");

//        Span subtitle = new Span();
//        subtitle.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
//        subtitle.setText(filename);

        Paragraph description = new Paragraph(
                extractMetadata(foldername+"/"+filename));
        description.addClassName(Margin.Vertical.MEDIUM);

//        Span badge = new Span();
//        badge.getElement().setAttribute("theme", "badge");
//        badge.setText("Label");

        // Добавляем кнопку для скачивания изображения
        Button downloadButton = new Button("Download");
        downloadButton.addClassNames(Margin.Top.MEDIUM);
        preview.add(downloadButton);

        // Устанавливаем ссылку для скачивания
        Anchor downloadLink = new Anchor(url, downloadButton);
        downloadLink.getElement().setAttribute("download", true); // Устанавливаем атрибут "download"
        add(div, description, /*subtitle, header, badge,*/ downloadLink);

    }

    private String extractMetadata(String filename) {
        try {
            File imageFile = new File("uploads/" + filename);
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

            StringBuilder sb = new StringBuilder();
            ExifIFD0Directory exif = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (exif != null) {
                sb.append("date: ").append(exif.getDescription(ExifIFD0Directory.TAG_DATETIME)).append("\n");
//                sb.append("Камера: ").append(exif.getDescription(ExifIFD0Directory.TAG_MAKE)).append(" ")
//                        .append(exif.getDescription(ExifIFD0Directory.TAG_MODEL)).append("\n");
            }

//            GpsDirectory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);
//            if (gps != null && gps.getGeoLocation() != null) {
//                sb.append("Геолокация: ").append(gps.getGeoLocation()).append("\n");
//            }

            return sb.toString().isEmpty() ? "Нет данных" : sb.toString();
        } catch (Exception e) {
            return "Ошибка при чтении EXIF";
        }
    }
}
