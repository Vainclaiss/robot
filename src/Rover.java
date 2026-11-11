import java.util.Objects;

public class Rover {

    private Coordinates position;
    private Direction direction;

    public Rover(Coordinates initialPosition, Direction initialDirection) {
        this.position = Objects.requireNonNull(initialPosition, "Position must be non null");
        this.direction = Objects.requireNonNull(initialDirection, "Direction must be non null");
    }

    public void turnLeft() {
        this.direction = this.direction.turnLeft();
    }

    public void turnRight() {
        this.direction = this.direction.turnRight();
    }

    // Computes target coordinate without mutating internal state
    public Coordinates nextPosition() {
        return position.move(direction.dx(), direction.dy());
    }

    // Applies the move once the controller validates bounds
    public void moveTo(Coordinates newPosition) {
        this.position = Objects.requireNonNull(newPosition, "Position must be non null");
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