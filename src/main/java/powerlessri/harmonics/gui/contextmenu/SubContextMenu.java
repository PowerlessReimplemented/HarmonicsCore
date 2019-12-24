package powerlessri.harmonics.gui.contextmenu;

import powerlessri.harmonics.gui.window.IControllableAppearance;

import java.awt.*;

public class SubContextMenu extends ContextMenu implements IControllableAppearance {

    private boolean hovered = false;

    public SubContextMenu() {
        super(0, 0);
        alive = false;
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        super.render(mouseX, mouseY, particleTicks);
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void kill() {
        // Only remove this sub-menu if
        // 1. cursor is not on the expanding entry (handled over there)
        // 2. cursor not in this context menu
        if (hovered) {
            return;
        }
        // 3. there are no alive sub-menus of this sub-menu
        for (Section section : getChildren()) {
            for (IEntry entry : section.getChildren()) {
                if (entry.forceAlive()) {
                    return;
                }
            }
        }
        alive = false;
    }

    @Override
    public void revive() {
        alive = true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        hovered = isInside(mouseX, mouseY);
    }
}
