package powerlessri.harmonics.gui.contextmenu;

import powerlessri.harmonics.gui.screen.WidgetScreen;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ContextMenuBuilder {

    public static final String GENERAL = "General";
    public static final String EDITING = "Editing";

    private ContextMenu contextMenu = null;
    private Map<String, Section> sections = new LinkedHashMap<>();

    public Section getSection(String name) {
        Section section = sections.get(name);
        if (section != null) {
            return section;
        }

        Section newSection = new Section();
        sections.put(name, newSection);
        getContextMenu().addSectionNoReflow(newSection);
        return newSection;
    }

    private ContextMenu getContextMenu() {
        if (contextMenu == null) {
            contextMenu = ContextMenu.atCursor();
        }
        return contextMenu;
    }

    public ContextMenu build() {
        ContextMenu menu = getContextMenu();
        menu.reflow();
        return menu;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ContextMenu buildAndAdd() {
        if (contextMenu != null) {
            contextMenu.reflow();
            WidgetScreen.assertActive().addPopupWindow(contextMenu);
            return contextMenu;
        }
        return null;
    }
}
