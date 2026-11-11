import java.util.Objects;

public class Rover {

    private Coordinates position;
    private Direction direction;
    private final Plateau plateau; // should he have access to plateau tho?

    public Rover(Coordinates initialPosition, Direction initialDirection, Plateau plateau) {
        this.plateau = Objects.requireNonNull(plateau, "Plateau must be non null");
        this.position = Objects.requireNonNull(initialPosition, "Position must be non null");
        this.direction = Objects.requireNonNull(initialDirection, "Direction must be non null");

        if (!plateau.isWithinBounds(initialPosition)) {
            throw new IllegalArgumentException(
                    "Initial position" + initialPosition + "is outside the plateau."
            );

        }
    }

    public void turnLeft() {
        this.direction = this.direction.turnLeft();

    }
    public void turnRight(){
        this.direction = this.direction.turnRight();
    }

    public void move() {
        Coordinates nextPosition = position.move(direction.dx() , direction.dy());

        if(!plateau.isWithinBounds(nextPosition)){
            throw new IllegalStateException(
                    "Cannot move outside plateau bounds to (" + nextPosition.x() + "," +  nextPosition.y());
        }

        this.position = nextPosition;

    }

    public Direction direction() {
        return direction;
    }

    public String formattedPosition(){
        return position.x() + " " + position.y() + " " + direction;
    }




}
