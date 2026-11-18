package service;

import records.Coordinates;

public class RectanglePlateau implements Plateau {

    private final int maxX;
    private final int maxY;

    public RectanglePlateau(int maxX, int maxY){
        if(maxX < 0 || maxY < 0) {
            throw new IllegalArgumentException("Plateau dimensions must be non negative (" + maxX + "," + maxY + ")");
        }
        this.maxX = maxX;
        this.maxY = maxY;
        }



    public boolean isWithinBounds(Coordinates coordinates) {
        if (coordinates == null) {
            return false;
        }
        return coordinates.x() >= 0 && coordinates.x() <= maxX && coordinates.y() >= 0 && coordinates.y() <= maxY;
    }

    public int maxX() { return maxX;}
    public int maxY() { return maxY;}



}
