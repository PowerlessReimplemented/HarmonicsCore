package powerlessri.harmonics.gui.contextmenu;

import com.google.common.collect.Lists;
import powerlessri.harmonics.gui.screen.WidgetScreen;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ContextMenuBuilder {

    public static final String GENERAL = "General";
    public static final String EDITING = "Editing";

    private Map<String, Section> sections = new LinkedHashMap<>();

    public Section getSection(String name) {
        Section section = sections.get(name);
        if (section != null) {
            return section;
        }

        Section newSection = new Section();
        sections.put(name, newSection);
        return newSection;
    }

    @Nullable
    public ContextMenu build() {
        if (sections.isEmpty()) {
            return null;
        }
        return ContextMenu.withSections(Lists.newArrayList(sections.values()));
    }

    public void buildAndAdd() {
        ContextMenu menu = build();
        if (menu != null) {
            WidgetScreen.assertActive().addPopupWindow(menu);
        }
    }
}
