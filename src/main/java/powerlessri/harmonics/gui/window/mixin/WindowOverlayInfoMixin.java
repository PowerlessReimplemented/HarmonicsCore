package powerlessri.harmonics.gui.window.mixin;

import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.Inspections;
import powerlessri.harmonics.gui.window.IWindow;

public interface WindowOverlayInfoMixin extends IWindow, Inspections.IInfoProvider, Inspections.IHighlightRenderer {

    @Override
    default void provideInformation(ITextReceiver receiver) {
        receiver.line(this.toString());
        receiver.line("Position=(" + getPosition().x + ", " + getPosition().y + ")");
        receiver.line("Dimensions=(" + getBorder().width + ", " + getBorder().width + ")");
        receiver.line("ContentPosition=(" + getContentX() + ", " + getContentY() + ")");
        receiver.line("ContentDimensions=(" + getContentWidth() + ", " + getContentHeight() + ")");
        receiver.line("BorderSize=" + getBorderSize());
        receiver.line("Z=" + getZLevel());
    }

    @Override
    default void renderHighlight() {
        Inspections.renderBorderedHighlight(
                getX(), getY(),
                getContentX(), getContentY(),
                getContentWidth(), getContentHeight(),
                getWidth(), getHeight());
    }
}
