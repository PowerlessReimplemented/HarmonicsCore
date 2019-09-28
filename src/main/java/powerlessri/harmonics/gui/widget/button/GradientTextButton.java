package powerlessri.harmonics.gui.widget.button;

import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.RenderingHelper;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;

public class GradientTextButton extends AbstractWidget implements IButton, LeafWidgetMixin {

    public static final int SIDE_MARGIN = 8;

    public static final int TOP_LEFT_COLOR = 0xffeeeeee;
    public static final int BOTTOM_RIGHT_COLOR = 0xff777777;
    public static final int GRADIENT_START_COLOR = 0xffe1e1e1;
    public static final int GRADIENT_END_COLOR = 0xffb1b1b1;

    public static final int DISABLED_TOP_LEFT_COLOR = 0xffeeeeee;
    public static final int DISABLED_BOTTOM_RIGHT_COLOR = 0xff777777;
    public static final int DISABLED_GRADIENT_START_COLOR = 0xffe1e1e1;
    public static final int DISABLED_GRADIENT_END_COLOR = 0xffb1b1b1;

    public static final int CLICKED_TOP_LEFT_COLOR = 0xff5c669d;
    public static final int CLICKED_BOTTOM_RIGHT_COLOR = 0xffbcc5ff;
    public static final int CLICKED_GRADIENT_START_COLOR = 0xff949ed4;
    public static final int CLICKED_GRADIENT_END_COLOR = 0xff6a74aa;

    public static final int HOVERING_TOP_LEFT_COLOR = 0xffa5aac5;
    public static final int HOVERING_BOTTOM_RIGHT_COLOR = 0xff999ebb;
    public static final int HOVERING_GRADIENT_START_COLOR = 0xffbabfda;
    public static final int HOVERING_GRADIENT_END_COLOR = 0xff8d92ad;

    public static final int TEXT_COLOR = 0xff303030;
    public static final int DISABLED_TEXT_COLOR = 0xff0a0a0;

    private String text;

    private boolean hovered;
    private boolean clicked;

    public GradientTextButton(String text) {
        this(0, 0, 0, 14, text);
        fitTextWidth();
    }

    public GradientTextButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @SuppressWarnings("UnusedReturnValue")
    public GradientTextButton setText(String text) {
        this.text = text;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public GradientTextButton fitTextWidth() {
        return fitTextWidth(SIDE_MARGIN);
    }

    @SuppressWarnings("UnusedReturnValue")
    public GradientTextButton fitTextWidth(int sideMargin) {
        int textWidth = fontRenderer().getStringWidth(text);
        setWidth(textWidth + sideMargin * 2);
        return this;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        clicked = true;
        setFocused(true);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        clicked = false;
        return true;
    }

    @Override
    public void onFocusChanged(boolean focus) {
        clicked = focus;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        hovered = isInside(mouseX, mouseY);
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);

        int x = getAbsoluteX();
        int y = getAbsoluteY();
        int x2 = getAbsoluteXRight();
        int y2 = getAbsoluteYBottom();
        RenderingHelper.drawRect(x, y, x2, y2, getBottomRightColor());
        RenderingHelper.drawRect(x, y, x2 - 1, y2 - 1, getTopLeftColor());
        RenderingHelper.drawVerticalGradientRect(x + 1, y + 1, x2 - 1, y2 - 1, getGradientStartColor(), getGradientEndColor());

        Render2D.renderCenteredText(text, y, y2, x, x2, getTextColor());

        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    @Override
    public boolean isHovered() {
        return hovered;
    }

    @Override
    public boolean isClicked() {
        return clicked;
    }

    public boolean isDisabled() {
        return !isEnabled();
    }

    public int getTopLeftColor() {
        return isDisabled() ? DISABLED_TOP_LEFT_COLOR
                : isClicked() ? CLICKED_TOP_LEFT_COLOR
                : isHovered() ? HOVERING_TOP_LEFT_COLOR
                : TOP_LEFT_COLOR;
    }

    public int getBottomRightColor() {
        return isDisabled() ? DISABLED_BOTTOM_RIGHT_COLOR
                : isClicked() ? CLICKED_BOTTOM_RIGHT_COLOR
                : isHovered() ? HOVERING_BOTTOM_RIGHT_COLOR
                : BOTTOM_RIGHT_COLOR;
    }

    public int getGradientStartColor() {
        return isDisabled() ? DISABLED_GRADIENT_START_COLOR
                : isClicked() ? CLICKED_GRADIENT_START_COLOR
                : isHovered() ? HOVERING_GRADIENT_START_COLOR
                : GRADIENT_START_COLOR;
    }

    public int getGradientEndColor() {
        return isDisabled() ? DISABLED_GRADIENT_END_COLOR
                : isClicked() ? CLICKED_GRADIENT_END_COLOR
                : isHovered() ? HOVERING_GRADIENT_END_COLOR
                : GRADIENT_END_COLOR;
    }

    public int getTextColor() {
        return isEnabled() ? TEXT_COLOR : DISABLED_TEXT_COLOR;
    }
}