package service;

import enums.Direction;
import records.Coordinates;

import java.util.Objects;

public class Rover {

    private Coordinates position;
    private Direction direction;

    public Rover(Coordinates initialPosition, Direction initialDirection) {
        this.position = Objects.requireNonNull(initialPosition, "Position must be non null");
        this.direction = Objects.requireNonNull(initialDirection, "enums.Direction must be non null");
    }

    public void turnLeft() {
        this.direction = this.direction.turnLeft();
    }

    public void turnRight() {
        this.direction = this.direction.turnRight();
    }

    public Coordinates nextPosition() {
        return position.move(direction.dx(), direction.dy());
    }

    public void moveTo(Coordinates newPosition) {

        if(newPosition == null) {
            throw new NullPointerException("Position must be non null");
        }

        this.position = newPosition;
    }

    public Coordinates position() {
        return position;
    }

    public Direction direction() {
        return direction;
    }

    @Override
    public String toString() {
        return position.x() + " " + position.y() + " " + direction;
    }
}