package powerlessri.harmonics.gui;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import powerlessri.harmonics.HarmonicsCore;

import static org.lwjgl.opengl.GL11.*;

// https://stackoverflow.com/questions/30254602/java-lwjgl-multiple-scissor-test
public final class ScissorTest {

    public static ScissorTest scaled(int x, int y, int width, int height) {
        MainWindow mainWindow = Minecraft.getInstance().mainWindow;
        double scale = mainWindow.getGuiScaleFactor();
        return new ScissorTest((int) (x * scale), (int) (mainWindow.getHeight() - ((y + height) * scale)),
                (int) (width * scale), (int) (height * scale));
    }

    private static final int max = 100;
    private static ScissorTest[] objects = new ScissorTest[max];
    private static int lastObject = -1;

    private int index;
    private int left;
    private int right;
    private int top;

    private int bottom;

    public ScissorTest(int x, int y, int width, int height) {
        lastObject++;
        if (lastObject < max) {
            index = lastObject;
            objects[index] = this;

            left = x;
            right = x + width - 1;
            top = y;
            bottom = y + height - 1;

            if (index > 0) {
                ScissorTest parent = objects[index - 1];

                if (left < parent.left) {
                    left = parent.left;
                }
                if (right > parent.right) {
                    right = parent.right;
                }
                if (top < parent.top) {
                    top = parent.top;
                }
                if (bottom > parent.bottom) {
                    bottom = parent.bottom;
                }
            }

            resume();
        } else {
            HarmonicsCore.logger.error("Scissor count limit reached: " + max);
        }
    }

    private void resume() {
        scissor(left, top, right - left + 1, bottom - top + 1);
        glEnable(GL_SCISSOR_TEST);
    }

    public void destroy() {
        if (index < lastObject) {
            HarmonicsCore.logger.error("There are scissors below this one");
        }

        glDisable(GL_SCISSOR_TEST);

        objects[index] = null;
        lastObject--;

        if (lastObject > -1) {
            objects[lastObject].resume(); // Resuming previous scissor
        }
    }

    private static void scissor(int x, int y, int width, int height) {
        if (width < 0) {
            width = 0;
        }
        if (height < 0) {
            height = 0;
        }
        glScissor(x, y, width, height);
    }
}
