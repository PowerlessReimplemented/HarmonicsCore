package powerlessri.harmonics.gui;

import powerlessri.harmonics.gui.style.IStyle;

import javax.annotation.Nullable;

public interface IStyledElement {

    /**
     * Get a style object with the corresponding type and identifier.
     * <p>
     * If no such style with the given type exists for this object, this method should return {@code null}. Otherwise it should return a
     * nonnull style object that has the identifier. If the parameter {@code identifier} is {@code -1}, this method should return the
     * first valid style object.
     * <p>
     * Recognizable identifiers are up to the implementation to choose. For example, {@link powerlessri.harmonics.gui.defined.TextField} has
     * three different box styles, and each of them corresponds to a static final identifier in {@link
     * powerlessri.harmonics.gui.defined.TextField}. If the user wants to get the background style, he would use
     * <pre>{@code
     *     textField.getStyle(IBoxStyle.class, TextField.BACKGROUND_STYLE);
     * }</pre>
     * It is strongly encouraged to always include a identifier even if the style you need is the default style. This is good for both
     * readability and future-proof.
     */
    @Nullable
    <S extends IStyle> S getStyle(Class<S> typeClass, int identifier);

    default <S extends IStyle> S getStyle(Class<S> typeClass) {
        return getStyle(typeClass, -1);
    }
}
