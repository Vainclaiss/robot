package records;

import enums.Direction;
import enums.Instruction;

import java.util.List;

public record RoverSpec(Coordinates startingPosition, Direction direction, List<Instruction> instructions) {}