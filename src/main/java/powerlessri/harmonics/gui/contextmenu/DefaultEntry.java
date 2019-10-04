package powerlessri.harmonics.gui.contextmenu;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.ResourceLocation;
import powerlessri.harmonics.gui.Render2D;
import powerlessri.harmonics.gui.debug.RenderEventDispatcher;
import powerlessri.harmonics.gui.widget.AbstractWidget;
import powerlessri.harmonics.gui.widget.mixin.LeafWidgetMixin;
import powerlessri.harmonics.gui.window.IWindow;

import javax.annotation.Nullable;
import java.awt.*;

import static powerlessri.harmonics.gui.Render2D.*;

public class DefaultEntry extends AbstractWidget implements IEntry, LeafWidgetMixin {

    public static final int MARGIN_SIDES = 2;
    public static final int HALF_MARGIN_SIDES = MARGIN_SIDES / 2;
    public static final int RENDERED_ICON_WIDTH = 8;
    public static final int RENDERED_ICON_HEIGHT = 8;

    private final ResourceLocation icon;
    private final String translationKey;

    public DefaultEntry(@Nullable ResourceLocation icon, String translationKey) {
        super(0, 0);
        this.icon = icon;
        this.translationKey = translationKey;
        Dimension bounds = getDimensions();
        bounds.width = computeWidth();
        bounds.height = computeHeight();
    }

    @Override
    public void render(int mouseX, int mouseY, float particleTicks) {
        RenderEventDispatcher.onPreRender(this, mouseX, mouseY);

        int x = getAbsoluteX();
        int y = getAbsoluteY();
        int y2 = getAbsoluteYBottom();
        if (isInside(mouseX, mouseY)) {
            IWindow parent = getWindow();
            GlStateManager.disableTexture();
            beginColoredQuad();
            coloredRect(x, y, parent.getContentX() + parent.getWidth() - parent.getBorderSize() * 2, y2, getZLevel(), 0xff3b86ff);
            draw();
            GlStateManager.enableTexture();
        }

        ResourceLocation icon = getIcon();
        if (icon != null) {
            int iconX = x + MARGIN_SIDES;
            int iconY = y + MARGIN_SIDES;

            completeTexture(iconX, iconY, iconX + RENDERED_ICON_WIDTH, iconY + RENDERED_ICON_HEIGHT, icon);
        }

        int textX = x + MARGIN_SIDES + RENDERED_ICON_WIDTH + 2;
        Render2D.renderVerticallyCenteredText(getText(), textX, y, y2, getZLevel(), 0xffffffff);
        RenderEventDispatcher.onPostRender(this, mouseX, mouseY);
    }

    @Nullable
    @Override
    public ResourceLocation getIcon() {
        return icon;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public void attach(ContextMenu contextMenu) {
        attachWindow(contextMenu);
    }

    public ContextMenu getContextMenu() {
        return (ContextMenu) super.getWindow();
    }

    private int computeWidth() {
        return MARGIN_SIDES + RENDERED_ICON_WIDTH + 2 + Render2D.fontRenderer().getStringWidth(getText()) + MARGIN_SIDES;
    }

    private int computeHeight() {
        return MARGIN_SIDES + RENDERED_ICON_HEIGHT + MARGIN_SIDES;
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        getContextMenu().discard();
        return true;
    }
}
