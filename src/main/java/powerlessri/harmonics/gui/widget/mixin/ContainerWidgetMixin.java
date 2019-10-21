package powerlessri.harmonics.gui.widget.mixin;

import powerlessri.harmonics.gui.widget.IContainer;
import powerlessri.harmonics.gui.widget.IWidget;

public interface ContainerWidgetMixin<T extends IWidget> extends IContainer<T> {

    default void renderChildren(int mouseX, int mouseY, float tickDelta) {
        for (T child : getChildren()) {
            child.render(mouseX, mouseY, tickDelta);
        }
    }

    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (T child : getChildren()) {
            if (child.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (T child : getChildren()) {
            if (child.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (T child : getChildren()) {
            if (child.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        for (T child : getChildren()) {
            if (child.mouseScrolled(mouseX, mouseY, scroll)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (T child : getChildren()) {
            if (child.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (T child : getChildren()) {
            if (child.keyReleased(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean charTyped(char charTyped, int keyCode) {
        for (T child : getChildren()) {
            if (child.charTyped(charTyped, keyCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    default void mouseMoved(double mouseX, double mouseY) {
        for (T child : getChildren()) {
            child.mouseMoved(mouseX, mouseY);
        }
    }

    @Override
    default void update(float tickDelta) {
        for (T child : getChildren()) {
            child.update(tickDelta);
        }
    }
}
