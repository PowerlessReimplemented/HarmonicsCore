package powerlessri.harmonics.gui;

public abstract class TextRenderers {

    TextRenderers() {
    }

    private static final VanillaTextRenderer VANILLA_TEXT_RENDERER = new VanillaTextRenderer();

    public static VanillaTextRenderer vanilla() {
        return VANILLA_TEXT_RENDERER;
    }

    public static VanillaTextRenderer newVanilla() {
        return new VanillaTextRenderer();
    }
}
