package powerlessri.harmonics.gui.style;

/**
 * Base interface for all style interfaces and implementations. This should be used as a relation bound in generics or subtyping for return
 * types.
 * <p>
 * An example of usage can be found in {@link powerlessri.harmonics.gui.IStyledElement}, its heterogeneous container pattern for inheriting
 * styles.
 */
public interface IStyle {

    /**
     * Helper method for casting to return type.
     */
    @SuppressWarnings("unchecked")
    default <S extends IStyle> S cast() {
        return (S) this;
    }
}
