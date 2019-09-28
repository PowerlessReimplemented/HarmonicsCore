package powerlessri.harmonics.gui.window;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import powerlessri.harmonics.gui.screen.BackgroundRenderers;
import powerlessri.harmonics.gui.widget.IWidget;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.layout.FlowLayout;
import powerlessri.harmonics.gui.screen.WidgetScreen;
import powerlessri.harmonics.gui.widget.TextField;
import powerlessri.harmonics.gui.widget.*;
import powerlessri.harmonics.gui.widget.box.Box;
import powerlessri.harmonics.gui.widget.button.ColoredTextButton;
import powerlessri.harmonics.gui.window.mixin.NestedEventHandlerMixin;
import powerlessri.harmonics.gui.window.mixin.WindowOverlayInfoMixin;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

import static powerlessri.harmonics.gui.screen.WidgetScreen.scaledHeight;
import static powerlessri.harmonics.gui.screen.WidgetScreen.scaledWidth;

public class Dialog implements IPopupWindow, NestedEventHandlerMixin, WindowOverlayInfoMixin {

    public static Dialog createPrompt(String message, BiConsumer<Integer, String> onConfirm) {
        return createPrompt(message, onConfirm, (b, t) -> {});
    }

    public static Dialog createPrompt(String message, BiConsumer<Integer, String> onConfirm, BiConsumer<Integer, String> onCancel) {
        return createPrompt(message, "", onConfirm, onCancel);
    }

    public static Dialog createPrompt(String message, String defaultText, BiConsumer<Integer, String> onConfirm) {
        return createPrompt(message, defaultText, onConfirm, (b, t) -> {});
    }

    public static Dialog createPrompt(String message, String defaultText, BiConsumer<Integer, String> onConfirm, BiConsumer<Integer, String> onCancel) {
        return createPrompt(message, defaultText, "gui.harmonics.Dialog.OK", "gui.harmonics.Dialog.Cancel", onConfirm, onCancel);
    }

    public static Dialog createPrompt(String message, String defaultText, String confirm, String cancel, BiConsumer<Integer, String> onConfirm, BiConsumer<Integer, String> onCancel) {
        Dialog dialog = dialog(message);

        TextField inputBox = new TextField(0, 0, 0, 16).setText(defaultText);
        inputBox.setBorderBottom(4);
        dialog.insertBeforeButtons(inputBox);
        dialog.onPostReflow = inputBox::expandHorizontally;

        dialog.buttons.addChildren(ColoredTextButton.of(confirm, b -> onConfirm.accept(b, inputBox.getText())));
        dialog.bindRemoveSelf2LastButton();
        dialog.buttons.addChildren(ColoredTextButton.of(cancel, b -> onCancel.accept(b, inputBox.getText())));
        dialog.bindRemoveSelf2LastButton();

        dialog.reflow();
        dialog.centralize();
        dialog.setFocusedWidget(inputBox);
        return dialog;
    }

    public static Dialog createBiSelectionDialog(String message, IntConsumer onConfirm) {
        return createBiSelectionDialog(message, onConfirm, ColoredTextButton.DUMMY);
    }

    public static Dialog createBiSelectionDialog(String message, IntConsumer onConfirm, IntConsumer onCancel) {
        return createBiSelectionDialog(message, "gui.harmonics.Dialog.OK", "gui.harmonics.Dialog.Cancel", onConfirm, onCancel);
    }

    public static Dialog createBiSelectionDialog(String message, String confirm, String cancel, IntConsumer onConfirm, IntConsumer onCancel) {
        Dialog dialog = dialog(message);

        dialog.buttons.addChildren(ColoredTextButton.of(confirm, onConfirm));
        dialog.bindRemoveSelf2LastButton();
        dialog.buttons.addChildren(ColoredTextButton.of(cancel, onCancel));
        dialog.bindRemoveSelf2LastButton();

        dialog.reflow();
        dialog.centralize();
        return dialog;
    }

    public static Dialog createDialog(String message) {
        return createDialog(message, ColoredTextButton.DUMMY);
    }

    public static Dialog createDialog(String message, IntConsumer onConfirm) {
        return createDialog(message, "gui.harmonics.Dialog.OK", onConfirm);
    }

    public static Dialog createDialog(String message, String ok, IntConsumer onConfirm) {
        Dialog dialog = dialog(message);

        dialog.buttons.addChildren(ColoredTextButton.of(ok, onConfirm));
        dialog.bindRemoveSelf2LastButton();

        dialog.reflow();
        dialog.centralize();
        return dialog;
    }

    private static Dialog dialog(String message) {
        Dialog dialog = new Dialog();
        dialog.messageBox.setBorderTop(5);
        dialog.messageBox.addTranslatedLine(message);
        return dialog;
    }

    public static final Consumer<Dialog> VANILLA_STYLE_RENDERER = d -> BackgroundRenderers.drawVanillaStyle(d.position.x, d.position.y, d.border.width, d.border.height, 0F);
    public static final int VANILLA_STYLE_BORDER_SIZE = 4;

    public static final Consumer<Dialog> FLAT_STYLE_RENDERER = d -> {
        GlStateManager.disableAlphaTest();
        BackgroundRenderers.drawFlatStyle(d.position.x, d.position.y, d.border.width, d.border.height, 0F);
        GlStateManager.enableAlphaTest();
    };
    public static final int FLAT_STYLE_BORDER_SIZE = 2 + 1;

    private final Point position;
    private final Dimension contents;
    private final Dimension border;

    private Consumer<Dialog> backgroundRenderer;
    private int borderSize;

    private Paragraph messageBox;
    private Box<ColoredTextButton> buttons;
    private List<AbstractWidget> children;

    public Runnable onPreReflow = () -> {};
    public Runnable onPostReflow = () -> {};

    private IWidget focusedWidget;
    private boolean alive = true;

    private int initialDragLocalX, initialDragLocalY;


    public Dialog() {
        this.position = new Point();
        this.contents = new Dimension();
        this.border = new Dimension();
        this.messageBox = new Paragraph(10, 10, new ArrayList<>());
        this.messageBox.setFitContents(true);
        this.buttons = new Box<ColoredTextButton>(0, 0, 10, 10)
                .setLayout(b -> {
                    int x = 0;
                    for (ColoredTextButton button : b) {
                        button.setLocation(x, 0);
                        x += button.getFullWidth() + 2;
                    }
                });
        this.children = new ArrayList<>();
        {
            children.add(messageBox);
            children.add(buttons);
        }
        this.useVanillaBorders();

        centralize();
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);
        backgroundRenderer.accept(this);
        for (IWidget child : children) {
            child.render(mouseX, mouseY, particleTicks);
        }
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    public void centralize() {
        position.x = scaledWidth() / 2 - getWidth() / 2;
        position.y = scaledHeight() / 2 - getHeight() / 2;
        notifyChildren();
    }

    public void reflow() {
        onPreReflow.run();
        messageBox.expandHorizontally();
        buttons.reflow();
        buttons.adjustMinContent();

        FlowLayout.reflow(children);

        updateDimensions();
        notifyChildren();
        onPostReflow.run();
    }

    private void updateDimensions() {
        updateContentDimensions();
        updateBorderDimensions();
    }

    private void updateContentDimensions() {
        int rightmost = 0;
        int bottommost = 0;
        for (IWidget child : children) {
            int right = child.getX() + child.getFullWidth();
            int bottom = child.getY() + child.getFullHeight();
            if (right > rightmost) {
                rightmost = right;
            }
            if (bottom > bottommost) {
                bottommost = bottom;
            }
        }
        contents.width = rightmost;
        contents.height = bottommost;
    }

    private void updateBorderDimensions() {
        border.width = contents.width + getBorderSize() * 2;
        border.height = contents.height + getBorderSize() * 2;
    }

    public void notifyChildren() {
        for (AbstractWidget child : children) {
            // This will update the absolute position as well (because we know all the children are at least AbstractWidget)
            // so no need to call child.onParentPositionChanged() here
            child.attachWindow(this);
        }
    }

    public Paragraph getMessageBox() {
        return messageBox;
    }

    public Box<ColoredTextButton> getButtons() {
        return buttons;
    }

    public void insertBeforeMessage(AbstractWidget widget) {
        children.add(0, widget);
    }

    public void insertBeforeButtons(AbstractWidget widget) {
        children.add(children.size() - 1, widget);
    }

    public void appendChild(AbstractWidget widget) {
        children.add(widget);
    }

    @Override
    public boolean shouldDiscard() {
        return !alive;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (NestedEventHandlerMixin.super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (isInside(mouseX, mouseY)) {
            setFocusedWidget(null);
            initialDragLocalX = (int) mouseX - position.x;
            initialDragLocalY = (int) mouseY - position.y;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (NestedEventHandlerMixin.super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        if (isInside(mouseX, mouseY)) {
            initialDragLocalX = -1;
            initialDragLocalY = -1;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (NestedEventHandlerMixin.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        if (isInside(mouseX, mouseY) && isDragging()) {
            position.x = (int) mouseX - initialDragLocalX;
            position.y = (int) mouseY - initialDragLocalY;
            notifyChildren();
            return true;
        }
        return false;
    }

    private boolean isDragging() {
        return initialDragLocalX != -1 && initialDragLocalY != -1;
    }

    @Override
    public Dimension getBorder() {
        return border;
    }

    @Override
    public int getBorderSize() {
        return borderSize;
    }

    @Override
    public Dimension getContents() {
        return contents;
    }

    @Override
    public List<? extends IWidget> getChildren() {
        return children;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Nullable
    @Override
    public IWidget getFocusedWidget() {
        return focusedWidget;
    }

    @Override
    public void setFocusedWidget(@Nullable IWidget widget) {
        focusedWidget = widget;
    }

    public void setStyle(Consumer<Dialog> renderer, int borderSize) {
        this.backgroundRenderer = renderer;
        this.borderSize = borderSize;
        reflow();
    }

    public void useFlatBorders() {
        setStyle(FLAT_STYLE_RENDERER, FLAT_STYLE_BORDER_SIZE);
    }

    public void useVanillaBorders() {
        setStyle(VANILLA_STYLE_RENDERER, VANILLA_STYLE_BORDER_SIZE);
    }

    public void bindRemoveSelf(int buttonID) {
        ColoredTextButton button = buttons.getChildren().get(buttonID);
        if (button.hasClickAction()) {
            IntConsumer oldAction = button.onClick;
            button.onClick = b -> {
                alive = false;
                oldAction.accept(b);
            };
        } else {
            button.onClick = b -> alive = false;
        }
    }

    public void bindRemoveSelf2LastButton() {
        bindRemoveSelf(buttons.getChildren().size() - 1);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean tryAddSelfToActiveGUI() {
        if (Minecraft.getInstance().currentScreen instanceof WidgetScreen) {
            addSelfTo(WidgetScreen.assertActive());
            return true;
        }
        return false;
    }

    public void addSelfTo(WidgetScreen gui) {
        gui.addPopupWindow(this);
    }
}
