package powerlessri.harmonics.gui.debug;

import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.window.IWindow;

import java.util.HashMap;
import java.util.Map;

public final class RenderEventDispatcher {

    private RenderEventDispatcher() {
    }

    public static final Map<Class<? extends IRenderEventListener>, IRenderEventListener> listeners = new HashMap<>();

    public static void onPreRender(IWidget widget, int mx, int my) {
        for (IRenderEventListener listener : listeners.values()) {
            listener.onPreRender(widget, mx, my);
        }
    }

    public static void onPreRender(IWindow window, int mx, int my) {
        for (IRenderEventListener listener : listeners.values()) {
            listener.onPreRender(window, mx, my);
        }
    }

    public static void onPostRender(IWidget widget, int mx, int my) {
        for (IRenderEventListener listener : listeners.values()) {
            listener.onPostRender(widget, mx, my);
        }
    }

    public static void onPostRender(IWindow window, int mx, int my) {
        for (IRenderEventListener listener : listeners.values()) {
            listener.onPostRender(window, mx, my);
        }
    }
}
