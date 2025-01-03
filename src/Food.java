import java.awt.*;
import java.util.Random;

public class Food {
    private int x;
    private int y;
    private static final int TILE_SIZE = 50;

    public void generate(int width, int height) {
        // Sprawdzenie, czy wymiary są wystarczająco duże
        if (width < TILE_SIZE || height < TILE_SIZE) {
            System.out.println("Warning: Dimensions are too small. Adjusting to minimum size.");
            width = TILE_SIZE;
            height = TILE_SIZE;
        }

        Random random = new Random();
        x = random.nextInt(width / TILE_SIZE) * TILE_SIZE;
        y = random.nextInt(height / TILE_SIZE) * TILE_SIZE;
    }

    public void draw(Graphics g, Image image) {
        if (image != null) {
            g.drawImage(image, x, y, TILE_SIZE, TILE_SIZE, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x, y, TILE_SIZE, TILE_SIZE);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}