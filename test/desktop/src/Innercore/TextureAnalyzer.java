package Innercore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class TextureAnalyzer {
    public void analyzeTexture(Texture texture) {
        // Create a FrameBuffer with the same dimensions as the texture
        FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, texture.getWidth(), texture.getHeight(), false);

        // Begin rendering to the FrameBuffer
        frameBuffer.begin();

        // Clear the FrameBuffer
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the texture onto the FrameBuffer
        // You can draw additional objects or perform other rendering operations here if needed
        // Example: batch.draw(texture, 0, 0);

        // End rendering to the FrameBuffer
        frameBuffer.end();

        // Get the pixel data from the FrameBuffer
        Pixmap pixmap = new Pixmap(texture.getWidth(), texture.getHeight(), Pixmap.Format.RGBA8888);
        Gdx.gl.glReadPixels(0, 0, texture.getWidth(), texture.getHeight(), GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixmap.getPixels());

        // Access the pixel data and perform analysis
        for (int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                // Get the color of the pixel at (x, y)
                int pixelColor = pixmap.getPixel(x, y);

                // Perform analysis based on the pixel color
                // Example: check if the pixel color matches a specific value
                if (pixelColor == 0xffffff) { // Check for red color
                    System.out.print(x);
                    System.out.print(':');
                    System.out.println(y);
                }
            }
        }

        // Dispose of the FrameBuffer and Pixmap when done

        pixmap.dispose();
    }

}


