import java.awt.*;

public class Snake {
    private static final int TILE_SIZE = 50;
    private int[] x;
    private int[] y;
    private int length;

    public Snake(int maxLength) {
        x = new int[maxLength];
        y = new int[maxLength];
        reset();
    }

    public void reset() {
        length = 2;
        for (int i = 0; i < length; i++) {
            x[i] = TILE_SIZE * (5 - i);
            y[i] = TILE_SIZE * 5;
        }
    }

    public void move(int dx, int dy) {
        for (int i = length - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        x[0] += dx;
        y[0] += dy;
    }

    public void grow() {
        length++;
    }

    public boolean checkCollision(int width, int height) {
        if (x[0] < 0 || x[0] >= width || y[0] < 0 || y[0] >= height) {
            return true;
        }
        for (int i = 1; i < length; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                return true;
            }
        }
        return false;
    }

    public int getX(int index) {
        return x[index];
    }

    public int getY(int index) {
        return y[index];
    }

    public int getLength() {
        return length;
    }
}