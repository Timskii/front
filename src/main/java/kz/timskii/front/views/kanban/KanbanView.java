package kz.timskii.front.views.kanban;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import javax.swing.text.View;

@Route("/kanban")
@PermitAll
public class KanbanView extends VerticalLayout {

    public KanbanView() {
        HorizontalLayout board = new HorizontalLayout();
        board.setWidthFull();

        // Колонки
        VerticalLayout todoColumn = createColumn("To Do");
        VerticalLayout inProgressColumn = createColumn("In Progress");
        VerticalLayout doneColumn = createColumn("Done");

        board.add(todoColumn, inProgressColumn, doneColumn);

        // Карточки
        todoColumn.add(createDraggableCard("Task 1"));
        todoColumn.add(createDraggableCard("Task 2"));
        inProgressColumn.add(createDraggableCard("Task 3"));

        Button plusButton = new Button(new Icon(VaadinIcon.PLUS));
        plusButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        plusButton.setAriaLabel("Add item");

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("New folder");

        TextField taskName = new TextField("task name");
        VerticalLayout dialogLayout = new VerticalLayout(taskName);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
               dialog.add(dialogLayout);

        Button saveButton = new Button("Add", e -> {
            dialog.close();
            Div div = new Div();
            div.add(new ViewCard(taskName.getValue(), "https://website.vaadin.com/hs-fs/hubfs/v-24-6-01.png?width=479&height=469&name=v-24-6-01.png"));
            DragSource<Div> dragSource = DragSource.create(div);
            dragSource.setDraggable(true);
            todoColumn.add(div);
        });
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        HorizontalLayout horizontalLayout = new HorizontalLayout(plusButton);
        add(horizontalLayout,dialog);

        plusButton.addClickListener(event -> {
            dialog.open();
        });


        add(plusButton, board);
    }

    private VerticalLayout createColumn(String title) {
        VerticalLayout column = new VerticalLayout();
        column.add(new H3(title));
        column.setWidth("30%");
        column.setHeight("1000px");
        column.getStyle().set("border", "1px solid #ccc").set("padding", "10px");

        // Добавляем DropTarget, чтобы можно было перетаскивать карточки в колонку
        DropTarget<VerticalLayout> dropTarget = DropTarget.create(column);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.addDropListener(event -> {
            event.getDragSourceComponent().ifPresent(column::add);
        });

        return column;
    }

    private Div createDraggableCard(String title) {
        Div card = new Div(new Text(title));
        card.getStyle()
                .set("padding", "20px")
                .set("border", "1px solid #aff")
                .set("margin", "5px")
                .set("background-color", "#aaa")
                .set("cursor", "grab");
        card.setWidth("80%");
        // Делаем карточку перетаскиваемой
        DragSource<Div> dragSource = DragSource.create(card);
        dragSource.setDraggable(true);

        return card;
    }
}
