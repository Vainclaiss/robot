package tests;

import enums.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import records.Coordinates;
import Entities.Rover;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Rover domain behavior")
class RoverTest {

    @Test
    @DisplayName("nextPosition() predicts the next coordinates without moving the rover")
    void nextPositionDoesNotMutateRoverState() {
        Rover rover = new Rover(new Coordinates(1, 2), Direction.N);

        Coordinates predicted = rover.nextPosition();

        assertEquals(new Coordinates(1, 3), predicted);
        assertEquals(new Coordinates(1, 2), rover.position());
    }

    @ParameterizedTest(name = "Facing {0} from (1,2) predicts ({1},{2})")
    @CsvSource({"N,1,3", "E,2,2", "S,1,1", "W,0,2"})
    void nextPositionFollowsDirection(Direction direction, int expectedX, int expectedY) {
        Rover rover = new Rover(new Coordinates(1, 2), direction);

        assertEquals(new Coordinates(expectedX, expectedY), rover.nextPosition());
    }

    @ParameterizedTest(name = "From {0}: turning left faces {1}, turning right faces {2}")
    @CsvSource({"N,W,E", "E,N,S", "S,E,W", "W,S,N"})
    void turnsLeftAndRight(Direction start, Direction expectedLeft, Direction expectedRight) {
        Rover leftRover = new Rover(new Coordinates(0, 0), start);
        leftRover.turnLeft();
        assertEquals(expectedLeft, leftRover.direction());

        Rover rightRover = new Rover(new Coordinates(0, 0), start);
        rightRover.turnRight();
        assertEquals(expectedRight, rightRover.direction());
    }


}