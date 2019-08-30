package powerlessri.harmonics.gui.window.mixin;

import powerlessri.harmonics.gui.IWindow;
import powerlessri.harmonics.gui.debug.ITextReceiver;
import powerlessri.harmonics.gui.debug.Inspections;

public interface WindowOverlayInfoMixin extends IWindow, Inspections.IInfoProvider {

    @Override
    default void provideInformation(ITextReceiver receiver) {
        receiver.line(this.toString());
        receiver.line("Position=" + this.getPosition());
        receiver.line("Dimensions=" + this.getBorder());
        receiver.line("BorderSize=" + this.getBorderSize());
        receiver.line("ContentDimensions=" + this.getContents());
        receiver.line("ContentX=" + this.getContentX());
        receiver.line("ContentY=" + this.getContentY());
    }
}
