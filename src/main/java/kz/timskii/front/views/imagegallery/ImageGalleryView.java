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
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import jakarta.annotation.security.PermitAll;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.io.File;

@PageTitle("Image Gallery")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.TH_LIST_SOLID)
@PermitAll
public class ImageGalleryView extends Main implements HasComponents, HasStyle {

    private OrderedList imageContainer;

    public ImageGalleryView() {
        constructUI();

        final File folder = new File("uploads/");

        // Проверяем наличие папки и файлов
        if (folder.exists() && folder.isDirectory()) {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile() && isImage(fileEntry.getName())) {
                    // Генерируем URL для доступа к изображению
                    String imageUrl = "/images?filename=" + fileEntry.getName();

                    // Создаем и добавляем карточку
                    imageContainer.add(new ImageGalleryViewCard(fileEntry.getName(), imageUrl));
                }
            }
        }

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
        imageContainer.addClassNames(Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.NONE, Padding.NONE);

        container.add(headerContainer, sortBy);
        add(container, imageContainer);

    }
    private boolean isImage(String fileName) {
        // Проверяем расширение файла
        String lowerName = fileName.toLowerCase();
        return lowerName.endsWith(".png") || lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") || lowerName.endsWith(".gif");
    }
}
