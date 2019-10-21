package powerlessri.harmonics.gui.widget;

import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

public class Spacer extends AbstractWidget implements LeafWidgetMixin {

    public Spacer(int width, int height) {
        this.setDimensions(width, height);
    }

    public Spacer(int x, int y, int width, int height) {
        this.setLocation(x, y);
        this.setDimensions(width, height);
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }
}
