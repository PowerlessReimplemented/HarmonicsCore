package powerlessri.harmonics.gui.widget.navigation;

import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

import static powerlessri.harmonics.gui.Render2D.fontRenderer;

public class DockedWindow extends AbstractWidget implements LeafWidgetMixin {

    private String name = "";

    public DockedWindow() {
        this.setDimensions(getSideMargin() * 2, 20);
    }

    public int getSideMargin() {
        return 4;
    }

    public int getTextColor() {
        return 0xffffffff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.setWidth(getSideMargin() + fontRenderer().getStringWidth(name) + getSideMargin());
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        Render2D.renderCenteredText(name, getAbsoluteY(), getAbsoluteYBottom(), getAbsoluteX(), getAbsoluteXRight(), getZLevel(), getTextColor());
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }
}
