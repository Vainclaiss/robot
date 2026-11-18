package tests;

import enums.Direction;
import enums.Instruction;
import exceptions.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import records.Coordinates;
import records.Mission;
import records.RoverSpec;
import service.RectanglePlateau;
import service.TextParser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TextParser input parsing and validation")
class TextParserTest {

    private final TextParser parser = new TextParser();

    @Test
    @DisplayName("Rejects empty input")
    void throwsWhenInputIsEmpty() {
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> parser.parse(List.of())
        );
        assertEquals("Input is empty", ex.getMessage());
    }

    @Test
    @DisplayName("Rejects input when a rover is missing its instruction line")
    void throwsWhenRoverInstructionsAreMissing() {
        List<String> input = List.of(
                "5 5",
                "1 2 N" // Missing instruction line
        );

        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> parser.parse(input)
        );
        assertTrue(ex.getMessage().contains("Each rover needs a position line followed by an instruction line"));
    }

    @ParameterizedTest(name = "Should reject invalid plateau input: \"{0}\"")
    @ValueSource(strings = {"5", "5 5 5", "A B", "-1 5"})
    void reportsLine1ErrorOnMalformedPlateau(String badPlateauLine) {
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> parser.parse(List.of(badPlateauLine, "1 2 N", "M"))
        );
        assertTrue(ex.getMessage().startsWith("Line 1:"));
    }

    @Test
    @DisplayName("Reports exact line number when an instruction character is invalid")
    void reportsAccurateLineNumberForInvalidInstruction() {
        List<String> input = List.of(
                "5 5",
                "1 2 N",
                "LMLXM" // 'X' is invalid on line 3
        );

        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> parser.parse(input)
        );
        assertTrue(ex.getMessage().contains("Line 3:"));
    }

    @Test
    @DisplayName("Reports exact line number when a subsequent rover has an invalid position")
    void reportsAccurateLineNumberForSecondRoverInvalidPosition() {
        List<String> input = List.of(
                "5 5",
                "1 2 N",
                "LM",
                "BAD POSITION LINE", // Line 4
                "MM"
        );

        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> parser.parse(input)
        );
        assertTrue(ex.getMessage().contains("Line 4:"));
    }

    @Test
    @DisplayName("Parses valid plateau dimensions and multiple rover specs")
    void parsesValidInput() {
        Mission mission = parser.parse(List.of("5 5", "1 2 N", "LM", "3 3 E", "MR"));

        RectanglePlateau plateau = (RectanglePlateau) mission.plateau();
        assertEquals(5, plateau.maxX());
        assertEquals(5, plateau.maxY());

        assertEquals(2, mission.rovers().size());
        RoverSpec first = mission.rovers().get(0);
        assertEquals(new Coordinates(1, 2), first.startingPosition());
        assertEquals(Direction.N, first.direction());
        assertEquals(List.of(Instruction.L, Instruction.M), first.instructions());
    }

    @Test
    @DisplayName("Tolerates trailing blank lines and extra whitespace between tokens")
    void toleratesTrailingBlankLinesAndExtraSpaces() {
        Mission mission = parser.parse(List.of("5   5", " 1 2  N ", "M", "", "  "));
        assertEquals(1, mission.rovers().size());
    }
}