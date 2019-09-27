package powerlessri.harmonics.gui.debug;

import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.window.IWindow;

public interface IRenderEventListener {

    void onPreRender(IWidget widget, int mx, int my);

    void onPreRender(IWindow window, int mx, int my);

    void onPostRender(IWidget widget, int mx, int my);

    void onPostRender(IWindow window, int mx, int my);
}
