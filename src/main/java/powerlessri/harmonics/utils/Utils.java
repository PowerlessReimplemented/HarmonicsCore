package powerlessri.harmonics.utils;

import java.util.Arrays;

/**
 * Collection of general helper methods that doesn't worth creating an extra helper class for them.
 */
public final class Utils {

    private Utils() {
    }

    /**
     * Semantic purposing method that directs to {@link Math#max(int, int)}.
     */
    public static int lowerBound(int n, int lowerBound) {
        return Math.max(n, lowerBound);
    }

    /**
     * Semantic purposing method that directs to {@link Math#min(int, int)}.
     */
    public static int upperBound(int n, int upperBound) {
        return Math.min(n, upperBound);
    }

    /**
     * Create an {@code end-start} long int array, where the first element is {@code start}, and each element after is 1 bigger than the
     * previous element.
     */
    public static int[] rangedIntArray(int start, int end) {
        int[] result = new int[end - start];
        Arrays.setAll(result, i -> i + start);
        return result;
    }
}
