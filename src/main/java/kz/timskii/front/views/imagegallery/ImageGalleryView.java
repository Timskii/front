package kz.timskii.front.views.imagegallery;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import jakarta.annotation.security.PermitAll;
import kz.timskii.front.services.FileService;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

@PageTitle("Image Gallery")
@Route("/image")
@Menu(order = 0, icon = LineAwesomeIconUrl.TH_LIST_SOLID)
@PermitAll
public class ImageGalleryView extends Main implements HasComponents, HasStyle, HasUrlParameter<String>  {

    private OrderedList imageContainer;
    private final FileService fileService;

    public ImageGalleryView(FileService fileService) {
        this.fileService = fileService;
        constructUI();

    }
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        String folder = queryParameters.getParameters()
                .getOrDefault("folder", List.of("uploads")) // Дефолтная папка
                .get(0);

        updateImages(folder);
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

        container.add(headerContainer, sortBy);
        add(container, imageContainer);

    }
}
