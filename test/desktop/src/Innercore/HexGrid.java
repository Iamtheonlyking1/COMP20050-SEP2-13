package Innercore;

import Innercore.Hexagon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class HexGrid {

    private static final float HEX_SIZE = 0; // Adjust the size of hexagons as needed

    private Texture hexTexture;
    private Hexagon[][] hexagons;

    public HexGrid() {
        hexTexture = new Texture(Gdx.files.internal("D:\\Software Enj 2\\test\\desktop\\src\\Innercore\\hex_tex.png")); // Change the path to your hex texture
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
            int randomRow = MathUtils.random(hexagons.length - 1);
            int randomCol = MathUtils.random(hexagons[randomRow].length - 1);

            Hexagon hexagon = hexagons[randomRow][randomCol];
            return hexagon.getPosition();
    }
}

    // Other methods for accessing hexagons, handling interaction, etc.

