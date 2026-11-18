package Entities;

import enums.Direction;
import records.Coordinates;

import java.util.Objects;

public class Rover {

    private Coordinates position;
    private Direction direction;


    public Rover(Coordinates initialPosition, Direction initialDirection) {
        this.position = Objects.requireNonNull(initialPosition, "Position must be non null");
        this.direction = Objects.requireNonNull(initialDirection, "Direction must be non null");

    }

    public void turnLeft() {
        this.direction = switch (this.direction) {
            case N -> Direction.W;
            case W -> Direction.S;
            case S -> Direction.E;
            case E -> Direction.N;
        };
    }

    public void turnRight() {
        this.direction = switch (this.direction) {
            case N -> Direction.E;
            case E -> Direction.S;
            case S -> Direction.W;
            case W -> Direction.N;
        };
    }


    public Coordinates nextPosition() {
        return switch (this.direction) {
            case N -> new Coordinates(position.x(), position.y() + 1);
            case E -> new Coordinates(position.x() + 1, position.y());
            case S -> new Coordinates(position.x(), position.y() - 1);
            case W -> new Coordinates(position.x() - 1, position.y());
        };
    }

    public void move() {
        this.position = nextPosition();
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