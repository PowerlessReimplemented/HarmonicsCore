package powerlessri.harmonics.gui;

public abstract class TextRenderer {

    TextRenderer() {
    }

    private static final VanillaTextRenderer VANILLA_TEXT_RENDERER = new VanillaTextRenderer();

    public static VanillaTextRenderer vanilla() {
        return VANILLA_TEXT_RENDERER;
    }
}
