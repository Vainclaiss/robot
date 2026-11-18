package tests;

import Entities.Plateau;
import Entities.RectanglePlateau;
import Entities.Rover;
import enums.Direction;
import enums.Instruction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import records.Coordinates;
import records.Mission;
import records.RoverSpec;
import service.MissionExecutor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MissionExecutor orchestration and boundary rules")
class MissionExecutorTest {

    @Test
    @DisplayName("Allows a move landing directly on the plateau's maximum boundary (5,5)")
    void allowsMoveLandingExactlyOnMaxBoundary() {
        Plateau plateau = new RectanglePlateau(5, 5);
        RoverSpec spec = new RoverSpec(new Coordinates(4, 5), Direction.E, List.of(Instruction.M));

        List<Rover> results = new MissionExecutor(new Mission(plateau, List.of(spec))).startMission();

        assertEquals(new Coordinates(5, 5), results.get(0).position());
        assertEquals(Direction.E, results.get(0).direction());
    }

    @Test
    @DisplayName("Keeps rover at starting position and heading when instructions are empty")
    void keepsRoverInPlaceWhenInstructionsAreEmpty() {
        Plateau plateau = new RectanglePlateau(5, 5);
        RoverSpec spec = new RoverSpec(new Coordinates(2, 3), Direction.S, List.of());

        List<Rover> results = new MissionExecutor(new Mission(plateau, List.of(spec))).startMission();

        assertEquals(new Coordinates(2, 3), results.get(0).position());
        assertEquals(Direction.S, results.get(0).direction());
    }

    @Test
    @DisplayName("Rejects deployment when rover starts outside the plateau")
    void throwsWhenRoverStartsOutsidePlateau() {
        Plateau plateau = new RectanglePlateau(5, 5);
        RoverSpec outside = new RoverSpec(new Coordinates(6, 2), Direction.N, List.of());
        MissionExecutor executor = new MissionExecutor(new Mission(plateau, List.of(outside)));

        IllegalStateException ex = assertThrows(IllegalStateException.class, executor::startMission);
        assertTrue(ex.getMessage().contains("outside the plateau"));
    }

    @ParameterizedTest(name = "Skips move {2} from ({0},{1}) that would leave the plateau, then turns to {3}")
    @CsvSource({
            "2, 5, N, E", // Top mid-edge
            "5, 2, E, S", // Right mid-edge
            "2, 0, S, W", // Bottom mid-edge
            "0, 2, W, N"  // Left mid-edge
    })
    void skipsMoveWhenRoverTriesToLeaveAnyEdge(int x, int y, Direction initialDirection, Direction expectedDirection) {
        Plateau plateau = new RectanglePlateau(5, 5);
        RoverSpec spec = new RoverSpec(
                new Coordinates(x, y),
                initialDirection,
                List.of(Instruction.M, Instruction.R)
        );
        MissionExecutor executor = new MissionExecutor(new Mission(plateau, List.of(spec)));

        List<Rover> results = executor.startMission();

        assertEquals(new Coordinates(x, y), results.get(0).position());
        assertEquals(expectedDirection, results.get(0).direction());
    }
}