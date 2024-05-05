package Innercore;

import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

public enum Direction {
    NORTH(0, 1),
    NORTHEAST(-1, 1),
    EAST(0, 1),
    SOUTHEAST(1, 1),
    SOUTH(0, -1),
    SOUTHWEST(1, -1),
    WEST(0, -1),
    NORTHWEST(-1, -1);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    public Direction reverse(Direction D){
        Direction X = null;
        if(D==Direction.EAST){
            X=Direction.WEST;
        } else if (D==Direction.NORTHEAST) {
            X=Direction.SOUTHWEST;
            
        } else if (D==Direction.WEST) {
            X=Direction.EAST;
            
        } else if (D==Direction.SOUTHWEST) {
            X=Direction.NORTHEAST;            
        }else if(D==Direction.NORTHWEST){
            X=Direction.SOUTHEAST;
        } else if (D==Direction.SOUTHEAST) {
            X=Direction.NORTHWEST;
        }
        return X;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

}
