package kz.timskii.front.views.folder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import kz.timskii.front.services.FileService;

import java.util.function.Consumer;

public class ButtonIcons extends Div {
    private FileService fileService;
    private TextField folderName;
    private final Consumer<String> onFolderCreated; // Callback для обновления SideNav

    public ButtonIcons(FileService fileService, Consumer<String> onFolderCreated) {
        this.fileService = fileService;
        this.onFolderCreated = onFolderCreated;
        // Icon button using an aria-label to provide a textual alternative
        // to screen readers
        Button plusButton = new Button(new Icon(VaadinIcon.PLUS));
        plusButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        plusButton.setAriaLabel("Add item");

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("New folder");

        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        Button saveButton = createSaveButton(dialog);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        HorizontalLayout horizontalLayout = new HorizontalLayout(plusButton);
        add(horizontalLayout,dialog);

        plusButton.addClickListener(event -> {
            dialog.open();
        });
    }


    private VerticalLayout createDialogLayout() {

        this.folderName = new TextField("Folder name");
        VerticalLayout dialogLayout = new VerticalLayout(this.folderName);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    private Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Add", e -> {
            dialog.close();
            String folderName = this.folderName.getValue().trim();
            if (!folderName.isEmpty()) {
                fileService.createFolder(folderName);
                onFolderCreated.accept(folderName); // 🔥 Вызываем обновление навигации
                dialog.close();
            } else {
                this.folderName.setInvalid(true);
                this.folderName.setErrorMessage("Folder name cannot be empty");
            }

        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return saveButton;
    }

}