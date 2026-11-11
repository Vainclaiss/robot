import java.util.List;

public record RoverSpec(Coordinates start, Direction heading, List<Instruction> instructions) {}