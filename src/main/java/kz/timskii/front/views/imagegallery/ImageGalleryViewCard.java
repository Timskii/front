package kz.timskii.front.views.imagegallery;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.theme.lumo.LumoUtility.*;


public class ImageGalleryViewCard extends ListItem {

    public ImageGalleryViewCard(String text, String url) {
        addClassNames(Background.CONTRAST_5, Display.FLEX, FlexDirection.COLUMN, AlignItems.START, Padding.MEDIUM,
                BorderRadius.LARGE);

        Div div = new Div();
        div.addClassNames(Background.CONTRAST, Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER,
                Margin.Bottom.MEDIUM, Overflow.HIDDEN, BorderRadius.MEDIUM, Width.FULL);
        div.setHeight("200px");

        Image image = new Image();
        image.setWidth("100%");
        image.setSrc(url);
        image.setAlt(text);

        div.add(image);

        Span header = new Span();
        header.addClassNames(FontSize.XLARGE, FontWeight.SEMIBOLD);
        header.setText("Title");

        Span subtitle = new Span();
        subtitle.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
        subtitle.setText(text);

        Paragraph description = new Paragraph(
                "Lorem ipsum dolor sit amet");
        description.addClassName(Margin.Vertical.MEDIUM);

        Span badge = new Span();
        badge.getElement().setAttribute("theme", "badge");
        badge.setText("Label");

        // Добавляем кнопку для скачивания изображения
        Button downloadButton = new Button("Download");
        downloadButton.addClassNames(Margin.Top.MEDIUM);

        // Устанавливаем ссылку для скачивания
        Anchor downloadLink = new Anchor(url, downloadButton);
        downloadLink.getElement().setAttribute("download", true); // Устанавливаем атрибут "download"
        add(div, header, subtitle, description, badge, downloadLink);

    }
}
