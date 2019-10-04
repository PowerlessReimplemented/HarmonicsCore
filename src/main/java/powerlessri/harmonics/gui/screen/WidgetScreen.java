package powerlessri.harmonics.gui.screen;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;
import powerlessri.harmonics.HarmonicsCore;
import powerlessri.harmonics.collections.CompositeUnmodifiableList;
import powerlessri.harmonics.gui.debug.Inspections;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.window.IPopupWindow;
import powerlessri.harmonics.gui.window.IWindow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class WidgetScreen extends Screen implements IGuiEventListener {

    /**
     * @throws ClassCastException If the current open screen is not a WidgetScreen
     */
    public static WidgetScreen assertActive() {
        return (WidgetScreen) Minecraft.getInstance().currentScreen;
    }

    @Nullable
    public static WidgetScreen activeNullable() {
        Screen screen = Minecraft.getInstance().currentScreen;
        if (screen instanceof WidgetScreen) {
            return (WidgetScreen) screen;
        }
        return null;
    }

    @Nonnull
    public static Optional<WidgetScreen> active() {
        Screen screen = Minecraft.getInstance().currentScreen;
        return Optional.ofNullable(screen instanceof WidgetScreen ? (WidgetScreen) screen : null);
    }

    private IWindow primaryWindow;
    private List<IWindow> regularWindows = new ArrayList<>();
    private List<IPopupWindow> popupWindows = new ArrayList<>();
    private Collection<IWindow> windows;

    private final WidgetTreeInspections inspectionHandler = new WidgetTreeInspections();

    protected WidgetScreen(ITextComponent title) {
        super(title);
        // Safe downwards erasure cast
        @SuppressWarnings("unchecked") List<IWindow> popupWindows = (List<IWindow>) (List<? extends IWindow>) this.popupWindows;
        windows = CompositeUnmodifiableList.of(regularWindows, popupWindows);
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
        popupWindows.removeIf(popup -> {
            if (popup.shouldDiscard()) {
                popup.onRemoved();
                return true;
            }
            return false;
        });

        float particleTicks = Minecraft.getInstance().getRenderPartialTicks();
        for (IWindow window : windows) {
            window.update(particleTicks);
        }
        primaryWindow.update(particleTicks);
    }

    protected final void setPrimaryWindow(IWindow primaryWindow) {
        Preconditions.checkState(this.primaryWindow == null, "Already initialized the primary window " + this.primaryWindow);
        this.primaryWindow = primaryWindow;
    }

    public final IWindow getPrimaryWindow() {
        return primaryWindow;
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        // Dark transparent overlay
        renderBackground();

        inspectionHandler.startCycle();
        GlStateManager.enableDepthTest();
        GlStateManager.enableAlphaTest();
        primaryWindow.render(mouseX, mouseY, particleTicks);
        for (IWindow window : windows) {
            window.render(mouseX, mouseY, particleTicks);
        }
        GlStateManager.disableDepthTest();
        inspectionHandler.endCycle();

        // This should do nothing because we are not adding vanilla buttons
        super.render(mouseX, mouseY, particleTicks);
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
        if (windows.stream().anyMatch(window -> window.mouseClicked(mouseX, mouseY, button))) {
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
        if (keyCode == GLFW.GLFW_KEY_E) {
            this.onClose();
            Minecraft.getInstance().player.closeScreen();
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
    }

    public void removePopupWindow(IPopupWindow popup) {
        popupWindows.remove(popup);
        popup.onRemoved();
    }
}
