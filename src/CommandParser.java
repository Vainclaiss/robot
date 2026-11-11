import java.util.List;
import java.util.Map;

public class CommandParser {

    private static final Map<Character, Command> COMMAND_REGISTRY = Map.of(
            'L', Rover::turnLeft,
            'R', Rover::turnRight,
            'M', Rover::move
    );

    public static Command parse(char token) {
        Command command = COMMAND_REGISTRY.get(token);
        if (command == null) {
            throw new IllegalArgumentException("Invalid command character: '" + token + "'");
        }
        return command;
    }

    public static List<Command> parseAll(String instructionString) {
        if (instructionString == null || instructionString.isBlank()) {
            return List.of();
        }

        return instructionString.trim()
                .chars()
                .mapToObj(c -> parse((char) c))
                .toList();
    }
}
