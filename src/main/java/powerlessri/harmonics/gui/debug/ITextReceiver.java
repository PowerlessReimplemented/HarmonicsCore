package powerlessri.harmonics.gui.debug;

public interface ITextReceiver {

    void reset();

    void string(String text);

    default void line(String line) {
        string(line);
        nextLine();
    }

    void nextLine();
}
