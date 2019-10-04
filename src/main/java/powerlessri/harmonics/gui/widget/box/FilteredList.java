package powerlessri.harmonics.gui.widget.box;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;
import powerlessri.harmonics.gui.widget.*;

import java.util.*;
import java.util.function.Consumer;

public class FilteredList<T extends IWidget & INamedElement> extends AbstractList<T> {

    public static <T extends IWidget & INamedElement> Pair<WrappingList, TextField> createSearchableList(List<T> list, String defaultText) {
        Pair<FilteredList<T>, TextField> pair = of(list, defaultText);
        FilteredList<T> filteredList = pair.getLeft();

        TextField textField = pair.getRight();
        WrappingList listView = new WrappingList();

        // Safe erasure downcast
        @SuppressWarnings("unchecked") List<IWidget> widgetList = (List<IWidget>) (List<? extends IWidget>) filteredList;
        listView.setContentList(widgetList);
        filteredList.onUpdate = l -> listView.reflow();

        return Pair.of(listView, textField);
    }

    public static <T extends IWidget & INamedElement> Pair<FilteredList<T>, TextField> of(List<T> list, String defaultText) {
        FilteredList<T> filteredList = new FilteredList<>(list);
        TextField textField = new TextField(0, 0, 64, 12) {
            @Override
            public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
                if (keyCode == GLFW.GLFW_KEY_ENTER) {
                    filteredList.updateSearch(getText());
                    return true;
                }
                return super.onKeyPressed(keyCode, scanCode, modifiers);
            }

            @Override
            public void onFocusChanged(boolean focus) {
                super.onFocusChanged(focus);
                if (!focus) {
                    filteredList.updateSearch(getText());
                }
            }
        }.setText(defaultText);
        filteredList.updateSearch(defaultText);
        return Pair.of(filteredList, textField);
    }

    private final List<T> backed;
    private List<T> searchResult;

    private Consumer<List<T>> onUpdate = l -> {};

    FilteredList(List<T> backed) {
        this.backed = backed;
        this.searchResult = backed;
    }

    public Consumer<List<T>> getOnUpdate() {
        return onUpdate;
    }

    public void setOnUpdate(Consumer<List<T>> onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void updateSearch(String search) {
        if (search.isEmpty()) {
            searchResult = backed;
        } else {
            searchResult = new ArrayList<>();
            for (T child : backed) {
                if (StringUtils.containsIgnoreCase(child.getName(), search)) {
                    searchResult.add(child);
                }
            }
        }
        onUpdate.accept(searchResult);
    }

    @Override
    public T get(int i) {
        return searchResult.get(i);
    }

    @Override
    public int size() {
        return searchResult.size();
    }
}
