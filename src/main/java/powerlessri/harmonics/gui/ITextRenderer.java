package powerlessri.harmonics.gui;

import java.util.List;

public interface ITextRenderer {

    void renderText(String text, int x, int y);

    void renderLines(List<String> text, int x, int y);

    int calculateWidth(String text);

    String trimToWidth(String text, int width);

    float getFontHeight();

    void setFontHeight(float fontHeight);

    int getTextColor();

    void setTextColor(int textColor);

    void useItalics(boolean italics);

    void useBold(boolean bold);

    void useStrikeThrough(boolean strikeThrough);
}
