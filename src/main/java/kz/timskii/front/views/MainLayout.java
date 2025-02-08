package kz.timskii.front.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import kz.timskii.front.data.User;
import kz.timskii.front.security.AuthenticatedUser;
import kz.timskii.front.services.FileService;
import kz.timskii.front.views.chat.ChatView;
import kz.timskii.front.views.folder.ButtonIcons;
import kz.timskii.front.views.imagegallery.ImageGalleryView;
import kz.timskii.front.views.profile.PersonFormView;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.ByteArrayInputStream;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, Component icon, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM, AlignItems.CENTER, Padding.Horizontal.SMALL,
                    TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames(FontWeight.MEDIUM, FontSize.MEDIUM, Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

    }

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;
    private FileService fileService;
    private SideNav nav; // Теперь SideNav хранится в поле
    private Scroller scroller; // Храним ссылку на Scroller

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker, FileService fileService) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        this.fileService = fileService;

        addToNavbar(createHeaderContent());
        addDrawerContent();
    }

    private void addDrawerContent() {
        Span appName = new Span("Навигация по папкам");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);
        nav = createNavigation();
        scroller = new Scroller(nav);

        addToDrawer(header, scroller, createFooter());
    }
    private Footer createFooter() {
        Footer layout = new Footer();
        ButtonIcons buttonIcons = new ButtonIcons(fileService, this::updateNavigation);
        layout.add(buttonIcons);
        return layout;
    }

    private void updateNavigation(String newFolder) {
        // Добавляем новый пункт в SideNav (без перерисовки всей навигации)
        SideNavItem newItem = new SideNavItem(newFolder, "image");
        newItem.setQueryParameters(QueryParameters.of("folder", newFolder));
        nav.addItem(newItem);

        // Пересоздаем Scroller с обновленным nav
        scroller.setContent(nav);
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        fileService.getFolders("uploads/")
                .forEach(folder -> {
                    SideNavItem sideNavItem = new SideNavItem(folder, "image");
                    sideNavItem.setQueryParameters(QueryParameters.of("folder", folder));
                    nav.addItem(sideNavItem);
                        });
        return nav;
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);

        Div layout = new Div();
        layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

        H1 appName = new H1("My App");
        appName.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, FontSize.LARGE);
        layout.add(appName);

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
//            StreamResource resource = new StreamResource("profile-pic",
//                    () -> new ByteArrayInputStream(user.getProfilePicture()));
//            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.addClassNames(Display.FLEX, AlignItems.CENTER, Gap.SMALL);
            userName.add(div);

            userName.getSubMenu().addItem("Profile", e -> {
                UI.getCurrent().navigate(PersonFormView.class);
            });

            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        Nav nav = new Nav();
        nav.addClassNames(Display.FLEX, Overflow.AUTO, Padding.Horizontal.MEDIUM, Padding.Vertical.XSMALL);

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames(Display.FLEX, Gap.SMALL, ListStyleType.NONE, Margin.NONE, Padding.NONE);
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            if (accessChecker.hasAccess(menuItem.getView())) {
                list.add(menuItem);
            }

        }

        header.add(layout, nav);
        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ //
                new MenuItemInfo("Image Gallery", LineAwesomeIcon.TH_LIST_SOLID.create(), ImageGalleryView.class),
                new MenuItemInfo("Person", LineAwesomeIcon.TH_LIST_SOLID.create(), PersonFormView.class),
                new MenuItemInfo("CHAT", LineAwesomeIcon.TH_LIST_SOLID.create(), ChatView.class),

        };
    }

}
