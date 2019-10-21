package powerlessri.harmonics.gui.screen;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import powerlessri.harmonics.HarmonicsCore;
import powerlessri.harmonics.collections.CompositeCollection;
import powerlessri.harmonics.gui.debug.Inspections;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.window.IPopupWindow;
import powerlessri.harmonics.gui.window.IWindow;

import java.util.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static powerlessri.harmonics.gui.Render2D.CONTEXT_MENU_Z;
import static powerlessri.harmonics.gui.Render2D.POPUP_WINDOW_Z;

public abstract class WidgetScreen extends Screen {

    /**
     * @throws ClassCastException If the current open screen is not a WidgetScreen
     */
    public static WidgetScreen assertActive() {
        return (WidgetScreen) MinecraftClient.getInstance().currentScreen;
    }

    @Nullable
    public static WidgetScreen activeNullable() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof WidgetScreen) {
            return (WidgetScreen) screen;
        }
        return null;
    }

    @NotNull
    public static Optional<WidgetScreen> active() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        return Optional.ofNullable(screen instanceof WidgetScreen ? (WidgetScreen) screen : null);
    }

    private IWindow primaryWindow;
    private List<IWindow> regularWindows = new ArrayList<>();
    private TreeSet<IPopupWindow> popupWindows = new TreeSet<>();
    private Collection<IWindow> windows;

    private final WidgetTreeInspections inspectionHandler = new WidgetTreeInspections();
    private final Queue<Triple<List<String>, Integer, Integer>> tooltipRenderQueue = new ArrayDeque<>();
    private final Queue<Runnable> taskQueue = new ArrayDeque<>();

    protected WidgetScreen(Text title) {
        super(title);
        // Safe downwards erasure cast
        @SuppressWarnings("unchecked") Collection<IWindow> popupWindowsView = (Collection<IWindow>) (Collection<? extends IWindow>) new DescendingTreeSetBackedUnmodifiableCollection<>(popupWindows);
        windows = new CompositeCollection<>(regularWindows, popupWindowsView);
    }

    @Override
    protected void init() {
        HarmonicsCore.logger.trace("(Re)initialized widget-based GUI {}", this);
        primaryWindow = null;
        regularWindows.clear();
        popupWindows.clear();
        RenderEventDispatcher.listeners.put(Inspections.class, inspectionHandler);
    }

    @Override
    public void tick() {
        while (!taskQueue.isEmpty()) {
            taskQueue.remove().run();
        }

        popupWindows.removeIf(popup -> {
            if (popup.shouldDiscard()) {
                popup.onRemoved();
                return true;
            }
            return false;
        });

        float tickDelta = MinecraftClient.getInstance().getTickDelta();
        for (IWindow window : windows) {
            window.update(tickDelta);
        }
        primaryWindow.update(tickDelta);
    }

    protected final void setPrimaryWindow(IWindow primaryWindow) {
        Preconditions.checkState(this.primaryWindow == null, "Already initialized the primary window " + this.primaryWindow);
        this.primaryWindow = primaryWindow;
    }

    public final IWindow getPrimaryWindow() {
        return primaryWindow;
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) {
        // Dark transparent overlay
        renderBackground();

        inspectionHandler.startCycle();
        GlStateManager.enableDepthTest();
        GlStateManager.enableAlphaTest();
        primaryWindow.render(mouseX, mouseY, tickDelta);
        for (IWindow window : regularWindows) {
            window.render(mouseX, mouseY, tickDelta);
        }
        // We want to render things away from the screen first (painter's algorithm)
        GlStateManager.pushMatrix();
        float zOff = CONTEXT_MENU_Z - POPUP_WINDOW_Z;
        for (IPopupWindow window : popupWindows) {
            window.render(mouseX, mouseY, tickDelta);
            GlStateManager.translatef(0F, 0F, zOff);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableDepthTest();
        inspectionHandler.endCycle();

        // This should do nothing because we are not adding vanilla buttons
        super.render(mouseX, mouseY, tickDelta);

        while (!tooltipRenderQueue.isEmpty()) {
            Triple<List<String>, Integer, Integer> entry = tooltipRenderQueue.remove();
            renderTooltip(entry.getLeft(), entry.getMiddle(), entry.getRight());
        }
    }

    public void addWindow(IWindow window) {
        regularWindows.add(window);
    }

    public void clearWindows() {
        regularWindows.forEach(IWindow::onRemoved);
        regularWindows.clear();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean captured = false;
        IPopupWindow capturedWindow = null;
        passEvents:
        {
            for (IWindow window : regularWindows) {
                if (window.mouseClicked(mouseX, mouseY, button)) {
                    captured = true;
                    break passEvents;
                }
            }
            for (IPopupWindow window : popupWindows) {
                if (window.mouseClicked(mouseX, mouseY, button)) {
                    capturedWindow = window;
                    captured = true;
                    break passEvents;
                }
            }
        }

        if (capturedWindow != null) {
            raiseWindowToTop(capturedWindow);
        }
        if (captured) {
            return true;
        } else {
            return primaryWindow.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (windows.stream().anyMatch(window -> window.mouseReleased(mouseX, mouseY, button))) {
            return true;
        } else {
            return primaryWindow.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragAmountX, double dragAmountY) {
        if (windows.stream().anyMatch(window -> window.mouseDragged(mouseX, mouseY, button, dragAmountX, dragAmountY))) {
            return true;
        } else {
            return primaryWindow.mouseDragged(mouseX, mouseY, button, dragAmountX, dragAmountY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amountScrolled) {
        if (windows.stream().anyMatch(window -> window.mouseScrolled(mouseX, mouseY, amountScrolled))) {
            return true;
        } else {
            return primaryWindow.mouseScrolled(mouseX, mouseY, amountScrolled);
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        for (IWindow window : windows) {
            window.mouseMoved(mouseX, mouseY);
        }
        primaryWindow.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (windows.stream().anyMatch(window -> window.keyPressed(keyCode, scanCode, modifiers))) {
            return true;
        } else if (primaryWindow.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (keyCode == GLFW_KEY_E) {
            this.onClose();
            MinecraftClient.getInstance().player.closeScreen();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (windows.stream().anyMatch(window -> window.keyReleased(keyCode, scanCode, modifiers))) {
            return true;
        } else {
            return primaryWindow.keyReleased(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean charTyped(char charTyped, int keyCode) {
        if (windows.stream().anyMatch(window -> window.charTyped(charTyped, keyCode))) {
            return true;
        } else {
            return primaryWindow.charTyped(charTyped, keyCode);
        }
    }

    @Override
    public void removed() {
        for (IWindow window : windows) {
            window.onRemoved();
        }
        primaryWindow.onRemoved();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void addPopupWindow(IPopupWindow popup) {
        popupWindows.add(popup);
        popup.onAdded(this);
        raiseWindowToTop(popup);
    }

    public void removePopupWindow(IPopupWindow popup) {
        popupWindows.remove(popup);
        popup.onRemoved();
    }

    public void defer(Runnable task) {
        taskQueue.add(task);
    }

    @SuppressWarnings("SuspiciousNameCombination") // Tuple3 is acting weird
    public void scheduleTooltip(List<String> lines, int x, int y) {
        tooltipRenderQueue.add(Triple.of(lines, x, y));
    }

    private int nextOrderIndex = 0;

    public void raiseWindowToTop(IPopupWindow window) {
        popupWindows.remove(window);
        window.setOrder(nextOrderIndex());
        popupWindows.add(window);
    }

    public int nextOrderIndex() {
        return nextOrderIndex++;
    }
}
