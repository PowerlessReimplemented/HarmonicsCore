package powerlessri.harmonics.gui.contextmenu;

import com.google.common.collect.Lists;

import java.util.*;

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

    public ContextMenu build() {
        return ContextMenu.withSections(Lists.newArrayList(sections.values()));
    }
}
