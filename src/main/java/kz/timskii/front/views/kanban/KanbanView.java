package kz.timskii.front.views.kanban;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

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

        add(board);
    }

    private VerticalLayout createColumn(String title) {
        VerticalLayout column = new VerticalLayout();
        column.add(new H3(title));
        column.setWidth("30%");
        column.setHeight("400px");
        column.getStyle().set("border", "1px solid #ccc").set("padding", "10px");

        // Добавляем DropTarget, чтобы можно было перетаскивать карточки в колонку
        DropTarget<VerticalLayout> dropTarget = DropTarget.create(column);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.addDropListener(event -> {
            event.getDragSourceComponent().ifPresent(card -> {
                column.add(card);
            });
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

        // Делаем карточку перетаскиваемой
        DragSource<Div> dragSource = DragSource.create(card);
        dragSource.setDraggable(true);

        return card;
    }
}
