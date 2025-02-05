package kz.timskii.front.views.imagegallery;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import jakarta.annotation.security.PermitAll;
import kz.timskii.front.services.FileService;
import kz.timskii.front.views.folder.ButtonIcons;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

@PageTitle("Image Gallery")
@Route("/image")
@Menu(order = 0, icon = LineAwesomeIconUrl.TH_LIST_SOLID)
@PermitAll
public class ImageGalleryView extends Main implements HasComponents, HasStyle, HasUrlParameter<String>  {

    private OrderedList imageContainer;
    private final FileService fileService;
    private String folderName;

    public ImageGalleryView(FileService fileService) {
        this.fileService = fileService;
        constructUI();

    }
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        folderName = queryParameters.getParameters()
                .getOrDefault("folder", List.of("uploads")) // Дефолтная папка
                .get(0);

        updateImages(folderName);
    }

    private void updateImages(String folder) {
        imageContainer.removeAll(); // Очищаем старые изображения
        fileService.getFilenamesByFolder(folder)
                .forEach((f, u) -> imageContainer.add(new ImageGalleryViewCard(f, u)));
    }

    private void constructUI() {
        addClassNames("image-gallery-view");
        addClassNames(MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(AlignItems.CENTER, JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Beautiful photos");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
        Paragraph description = new Paragraph("Royalty free photos and pictures, courtesy of Unsplash");
        description.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
        headerContainer.add(header, description);

        Select<String> sortBy = new Select<>();
        sortBy.setLabel("Sort by");
        sortBy.setItems("Popularity", "Newest first", "Oldest first");
        sortBy.setValue("Popularity");

        imageContainer = new OrderedList();
        imageContainer.addClassNames(Gap.SMALL, Display.GRID, ListStyleType.NONE, Margin.MEDIUM, Padding.NONE);

        container.add(headerContainer, sortBy, createFooter());

        add(container, imageContainer );

    }


    private Footer createFooter() {
        Footer layout = new Footer();
        layout.add(createUpload());
        return layout;
    }

    private Upload createUpload() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);

        Span dropLabel = createDropLabel();
        Icon dropIcon = VaadinIcon.CLOUD_UPLOAD_O.create();

        upload.setDropLabel(dropLabel);
        upload.setDropLabelIcon(dropIcon);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();

            fileService.upload(buffer.getInputStream(fileName), fileName, folderName);
        });


        return upload;
    }

    private static Span createDropLabel() {
        Span cloudHint = new Span(
                "грузим файлы");

        return new Span(cloudHint);
    }
}
