package Innercore;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class ray {
    private ShapeRenderer shapeRenderer;

    public ray() {
        shapeRenderer = new ShapeRenderer();
    }

    public void renderLine(Vector2 startPoint, float angle, float mapWidth, float mapHeight) {
        // Calculate the direction vector of the line based on the angle
        float endX = startPoint.x + mapWidth * (float) Math.cos(Math.toRadians(angle));
        float endY = startPoint.y + mapHeight * (float) Math.sin(Math.toRadians(angle));

        // Clip the line to the bounds of the hexagonal map texture
        Vector2 clippedEnd = clipLineToBounds(startPoint, new Vector2(endX, endY), mapWidth, mapHeight);

        // Draw the line
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED); // Set line color
        shapeRenderer.line(startPoint.x, startPoint.y, clippedEnd.x, clippedEnd.y);
        shapeRenderer.end();
    }

    // Helper method to clip the line to the bounds of the hexagonal map texture
    private Vector2 clipLineToBounds(Vector2 startPoint, Vector2 endPoint, float mapWidth, float mapHeight) {
        Vector2 clippedEnd = new Vector2(endPoint);

        // Calculate the boundary points of the pentagon-shaped map texture
        Vector2 topVertex = new Vector2(mapWidth / 2f, mapHeight);
        Vector2 bottomLeftVertex = new Vector2(0, 0);
        Vector2 bottomRightVertex = new Vector2(mapWidth, 0);
        Vector2 leftVertex = new Vector2(mapWidth / 4f, 0);
        Vector2 rightVertex = new Vector2(mapWidth * 3f / 4f, 0);

        // Clip the line against each side of the pentagon-shaped map texture
        clippedEnd = clipLine(startPoint, clippedEnd, topVertex, leftVertex);
        clippedEnd = clipLine(startPoint, clippedEnd, leftVertex, bottomLeftVertex);
        clippedEnd = clipLine(startPoint, clippedEnd, bottomLeftVertex, bottomRightVertex);
        clippedEnd = clipLine(startPoint, clippedEnd, bottomRightVertex, rightVertex);
        clippedEnd = clipLine(startPoint, clippedEnd, rightVertex, topVertex);

        return clippedEnd;
    }

    // Helper method to clip a line segment against a line defined by two points
    private Vector2 clipLine(Vector2 startPoint, Vector2 endPoint, Vector2 clipStart, Vector2 clipEnd) {
        float dx = endPoint.x - startPoint.x;
        float dy = endPoint.y - startPoint.y;

        // Calculate the parametric values of intersection points along the line segment
        float p1 = -dx;
        float p2 = dx;
        float p3 = -dy;
        float p4 = dy;

        float q1 = startPoint.x - clipStart.x;
        float q2 = clipEnd.x - startPoint.x;
        float q3 = startPoint.y - clipStart.y;
        float q4 = clipEnd.y - startPoint.y;

        // Calculate the parametric values of intersection points along the clip line
        float r1 = q1 / p1;
        float r2 = q2 / p2;
        float r3 = q3 / p3;
        float r4 = q4 / p4;

        // Find the maximum and minimum parametric values to determine the intersection points
        float maxLow = Math.max(Math.min(r1, r2), Math.min(r3, r4));
        float minHigh = Math.min(Math.max(r1, r2), Math.max(r3, r4));

        // If the maximum low parametric value is greater than the minimum high parametric value,
        // then the line segment does not intersect with the clip line
        if (maxLow > minHigh) {
            return new Vector2(endPoint);
        }

        // Calculate the intersection point
        float x = startPoint.x + maxLow * dx;
        float y = startPoint.y + maxLow * dy;

        return new Vector2(x, y);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

