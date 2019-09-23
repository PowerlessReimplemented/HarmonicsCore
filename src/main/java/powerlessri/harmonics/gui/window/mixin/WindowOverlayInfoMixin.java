package powerlessri.harmonics.gui.window.mixin;

import powerlessri.harmonics.gui.IWindow;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.Inspections;

public interface WindowOverlayInfoMixin extends IWindow, Inspections.IInfoProvider {

    @Override
    default void provideInformation(ITextReceiver receiver) {
        receiver.line(this.toString());
        receiver.line("Position=(" + getPosition().x + ", " + getPosition().y + ")");
        receiver.line("Dimensions=(" + getBorder().width + ", " + getBorder().width + ")");
        receiver.line("ContentPosition=(" + getContentX() + ", " + getContentY() + ")");
        receiver.line("ContentDimensions=(" + getContentWidth() + ", " + getContentHeight() + ")");
        receiver.line("BorderSize=" + this.getBorderSize());
    }
}
