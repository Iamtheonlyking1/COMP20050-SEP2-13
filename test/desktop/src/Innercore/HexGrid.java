package Innercore;

import Innercore.Hexagon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class HexGrid {

    private static final float HEX_SIZE = 0; // Adjust the size of hexagons as needed

    public Texture hexTexture;
    private Hexagon[][] hexagons;
    private static final float HEX_WIDTH = 30;  // Adjust as needed
    private static final float HEX_HEIGHT = 30; // Adjust as needed

    // Constants defining the horizontal and vertical spacing between hexagons
    private static final float HORIZONTAL_SPACING = HEX_WIDTH * 3 / 4;
    private static final float VERTICAL_SPACING = HEX_HEIGHT * (float) Math.sqrt(3) / 2;


    public HexGrid() {
        hexTexture = new Texture(Gdx.files.internal("hex_new.png")); // Change the path to your hex texture
        hexagons = new Hexagon[10][10]; // Example: 10x10 grid

        float startX = 0; // Adjust as needed
        float startY = 0; // Adjust as needed
        float xOffset = HEX_SIZE * 3 / 4f;
        float yOffset = HEX_SIZE * (float) Math.sqrt(3) / 2f;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                float x = startX + j * xOffset;
                float y = startY + i * yOffset;
                if (j % 2 == 1) {
                    y += yOffset / 2f;
                }
                hexagons[i][j] = new Hexagon(new Vector2(x, y), hexTexture);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Hexagon[] row : hexagons) {
            for (Hexagon hexagon : row) {
                hexagon.render(batch);
            }
        }
    }
    public Vector2 getRandomHexagonPosition() {
       return new Vector2();
    }
    public Vector2 calculateHexagonPosition(double row, double col) {
        double offsetX = col * HORIZONTAL_SPACING;
        double offsetY = row * VERTICAL_SPACING;

        // For odd rows, shift hexagons half the width to the right
        if (row % 2 != 0) {
            offsetX += HORIZONTAL_SPACING / 2;
        }

        return new Vector2((float) offsetX, (float) offsetY);
    }
}

    // Other methods for accessing hexagons, handling interaction, etc.

