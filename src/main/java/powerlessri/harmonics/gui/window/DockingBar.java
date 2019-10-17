package powerlessri.harmonics.gui.window;

import com.google.common.collect.ImmutableList;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.widget.navigation.DockedWindow;
import powerlessri.harmonics.gui.widget.panel.HorizontalList;

import java.util.ArrayList;
import java.util.List;

import static powerlessri.harmonics.gui.Render2D.scaledHeight;

public class DockingBar extends AbstractWindow {

    private final HorizontalList<DockedWindow> dockedWindows;
    private final List<IWidget> children;

    public DockingBar(int width, int height) {
        this.setBorder(width, height);
        this.dockedWindows = new HorizontalList<DockedWindow>(getContentWidth(), getContentHeight()) {
            @Override
            public int getBarHeight() {
                return 3;
            }
        };
        this.dockedWindows.attachWindow(this);
        this.children = ImmutableList.of(dockedWindows);
    }

    public void addDockedWindow(DockedWindow item) {
        int height = dockedWindows.getHeight() - dockedWindows.getBarHeight();
        item.setHeight(height);
        dockedWindows.addChildren(item);
        dockedWindows.reflow();
    }

    @Override
    public int getBorderSize() {
        return 2;
    }

    @Override
    public List<? extends IWidget> getChildren() {
        return children;
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        renderFlatStyleBackground();
        renderChildren(mouseX, mouseY, particleTicks);
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }
}
