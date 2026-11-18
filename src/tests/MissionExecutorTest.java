package tests;

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
import service.Plateau;
import service.RectanglePlateau;
import service.Rover;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MissionExecutor orchestration and boundary rules")
class MissionExecutorTest {

    @Test
    @DisplayName("Allows a move landing directly on the plateau's maximum boundary (5,5)")
    void allowsMoveLandingExactlyOnMaxBoundary() {
        Plateau plateau = new RectanglePlateau(5, 5);
        RoverSpec spec = new RoverSpec(new Coordinates(4, 5), Direction.E, List.of(Instruction.M));

        List<Rover> results = new MissionExecutor(new Mission(plateau, List.of(spec))).run();

        assertEquals(new Coordinates(5, 5), results.get(0).position());
        assertEquals(Direction.E, results.get(0).direction());
    }

    @Test
    @DisplayName("Keeps rover at starting position and heading when instructions are empty")
    void keepsRoverInPlaceWhenInstructionsAreEmpty() {
        Plateau plateau = new RectanglePlateau(5, 5);
        RoverSpec spec = new RoverSpec(new Coordinates(2, 3), Direction.S, List.of());

        List<Rover> results = new MissionExecutor(new Mission(plateau, List.of(spec))).run();

        assertEquals(new Coordinates(2, 3), results.get(0).position());
        assertEquals(Direction.S, results.get(0).direction());
    }

    @Test
    @DisplayName("Rejects deployment when rover starts outside the plateau")
    void throwsWhenRoverStartsOutsidePlateau() {
        Plateau plateau = new RectanglePlateau(5, 5);
        RoverSpec outside = new RoverSpec(new Coordinates(6, 2), Direction.N, List.of());
        MissionExecutor executor = new MissionExecutor(new Mission(plateau, List.of(outside)));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, executor::run);
        assertTrue(ex.getMessage().contains("outside the plateau"));
    }

    @ParameterizedTest(name = "Rejects move {2} from ({0},{1}) that leaves the plateau")
    @CsvSource({
            "2, 5, N", // Top mid-edge
            "5, 2, E", // Right mid-edge
            "2, 0, S", // Bottom mid-edge
            "0, 2, W"  // Left mid-edge
    })
    void throwsWhenRoverMovesOffAnyEdge(int x, int y, Direction direction) {
        Plateau plateau = new RectanglePlateau(5, 5);
        RoverSpec spec = new RoverSpec(new Coordinates(x, y), direction, List.of(Instruction.M));
        MissionExecutor executor = new MissionExecutor(new Mission(plateau, List.of(spec)));

        IllegalStateException ex = assertThrows(IllegalStateException.class, executor::run);
        assertTrue(ex.getMessage().contains("leaves the plateau"));
    }
}