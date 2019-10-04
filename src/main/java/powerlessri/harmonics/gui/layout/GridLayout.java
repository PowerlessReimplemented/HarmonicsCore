package powerlessri.harmonics.gui.layout;

/**
 * Layout widgets on a non-fixed dimension grid, where each row and column have their individual size. The widgets are layed on the grid so
 * that they cover a rectangle of cells.
 * <p>
 * See CSS Grid Layout. This class is meant to replicate the mechanics of it.
 */
public class GridLayout {

//    public class Resolver implements IFractionalLengthHandler {
//
//        private Resolver() {
//        }
//
//        private int denominator;
//
//        @Override
//        public int getDenominator() {
//            return denominator;
//        }
//    }
//
//    private IContainer<?> bondWidget;
//    private Length<?> gridGap;
//    private Length<Resolver>[] rowHeights;
//    private Length<Resolver>[] columnWidths;
//
//    private Resolver widthResolver = new Resolver();
//    private Resolver heightResolver = new Resolver();
//
//    /**
//     * A map of where the child widgets should occupy. See CSS Grid's {@code grid-template-areas}. Each element is the ID (index) of the
//     * child widget. Note that this array should be setup in [y][x], not [x][y].
//     */
//    private int[][] areas;
//
//    public GridLayout(IContainer<?> bondWidget) {
//        this.bondWidget = bondWidget;
//    }
//
//    @SuppressWarnings("UnusedReturnValue")
//    public GridLayout gridGap(Length<?> gridGap) {
//        setGridGap(gridGap);
//        return this;
//    }
//
//    @SuppressWarnings("UnusedReturnValue")
//    public GridLayout rows(Length<Resolver> rows) {
//        setRows(rows);
//        return this;
//    }
//
//    @SuppressWarnings("UnusedReturnValue")
//    public GridLayout columns(Length<Resolver> columns) {
//        setColumns(columns);
//        return this;
//    }
//
//    /**
//     * The width of each column will be {@code n/s} pixels of the width of the bond widget minus the gaps, where {@code n} is the array
//     * element, {@code s} is the <i>sum</i> of all elements in the array.
//     */
//    @SuppressWarnings("UnusedReturnValue")
//    public GridLayout widths(int... widthFactors) {
//        setColumnWidths(widthFactors);
//        return this;
//    }
//
//    /**
//     * The width of each row will be {@code n/s} pixels of the height of the bond widget minus the gaps, where {@code n} is the array
//     * element, {@code s} is the <i>sum</i> of all elements in the array.
//     */
//    @SuppressWarnings("UnusedReturnValue")
//    public GridLayout heights(int... heightFactors) {
//        setRowHeights(heightFactors);
//        return this;
//    }
//
//    @SuppressWarnings("UnusedReturnValue")
//    public GridLayout areas(int... areas) {
//        this.setAreas(areas);
//        return this;
//    }
//
//    /**
//     * A special version of the regular reflow mechanism, where areas can be named. The names should come from the widgets; if some names
//     * are not defined, it will use the first widget in the list by default.
//     */
//    public <T extends IWidget & INamedElement & ResizableWidgetMixin> void reflow(List<T> widgets, String[] template) {
//        int[][] areas = new int[rows][columns];
//        Object2IntMap<String> m = new Object2IntOpenHashMap<>();
//        for (int i = 0; i < widgets.size(); i++) {
//            T widget = widgets.get(i);
//            m.put(widget.getName(), i);
//        }
//        // Check for no repeating names
//        Preconditions.checkArgument(m.size() == widgets.size());
//
//        for (int y = 0; y < rows; y++) {
//            for (int x = 0; x < columns; x++) {
//                areas[y][x] = m.getInt(template[y * columns + x]);
//            }
//        }
//        reflow(widgets, areas);
//    }
//
//    public <T extends IWidget & ResizableWidgetMixin> void reflow(List<T> widgets) {
//        reflow(widgets, this.areas);
//    }
//
//    // px/py stands for "Pixel-position x/y"
//    // gx/gy stands for "Grid x/y"
//    public <T extends IWidget & ResizableWidgetMixin> void reflow(List<T> widgets, int[][] areas) {
//        for (int gy = 0; gy < areas.length; gy++) {
//            int[] row = areas[gy];
//            for (int gx = 0; gx < row.length; gx++) {
//                int cell = row[gx];
//                // Discard nonexistent indexes
//                if (cell > widgets.size()) {
//                    continue;
//                }
//
//                T widget = widgets.get(cell);
//                if (!BoxSizing.shouldIncludeWidget(widget)) {
//                    continue;
//                }
//
//                int px = getPxAt(gx);
//                int py = getPyAt(gy);
//                // Expand the first vertex towards top left
//                if (!widget.isInside(px, py)) {
//                    widget.setLocation(Math.min(widget.getX(), py), Math.min(widget.getY(), py));
//                }
//
//                int px2 = getPx2At(gx);
//                int py2 = getPy2At(gy);
//                // Expand the second vertex towards bottom right
//                if (!widget.isInside(px2, py2)) {
//                    widget.setDimensions(Math.max(widget.getFullWidth(), px2 - px), Math.max(widget.getFullHeight(), py2 - py));
//                }
//            }
//        }
//    }
//
//    private int getPxAt(int gx) {
//        int result = 0;
//        for (int i = 0; i < gx; i++) {
//            result += columnWidths[i] + gridGap;
//        }
//        return result;
//    }
//
//    private int getPyAt(int gy) {
//        int result = 0;
//        for (int i = 0; i < gy; i++) {
//            result += rowHeights[i] + gridGap;
//        }
//        return result;
//    }
//
//    private int getPx2At(int gx) {
//        return getPxAt(gx) + columnWidths[gx];
//    }
//
//    private int getPy2At(int gy) {
//        return getPyAt(gy) + rowHeights[gy];
//    }
//
//    public IContainer<?> getBondWidget() {
//        return bondWidget;
//    }
//
//    public Dimension getDimensions() {
//        return bondWidget.getDimensions();
//    }
//
//    public int getGridGap() {
//        return gridGap.getInt();
//    }
//
//    private int getHorizontalSumGaps() {
//        return (columnWidths.length - 1) * gridGap.getInt();
//    }
//
//    private int getVerticalSumGaps() {
//        return (rowHeights.length - 1) * gridGap.getInt();
//    }
//
//    public void setGridGap(Length<?> gridGap) {
//        Preconditions.checkArgument(gridGap.getInt() >= 0);
//        this.gridGap = gridGap;
//    }
//
//    public void setRows(Length<Resolver>... rows) {
//        this.rowHeights = rows;
//    }
//
//    public void setColumns(Length<Resolver>... columns) {
//        this.columnWidths = columns;
//    }
//
//    public void setAreas(int[] areas) {
//        Preconditions.checkArgument(areas.length == rows * columns);
//        this.areas = new int[rows][columns];
//        for (int y = 0; y < rows; y++) {
//            System.arraycopy(areas, y * columns, this.areas[y], 0, rows);
//        }
//    }
}
