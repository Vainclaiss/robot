import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MissionControl {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar rover.jar <input-file-path>");
            System.exit(1);
        }

        String filePath = args[0];

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // 1. Parse Plateau dimensions (Line 1)
            String plateauLine = reader.readLine();
            if (plateauLine == null || plateauLine.isBlank()) {
                System.err.println("Error: Input file is empty.");
                return;
            }

            Plateau plateau = parsePlateau(plateauLine);


            String positionLine;
            while ((positionLine = reader.readLine()) != null) {
                if (positionLine.isBlank()) continue;

                String instructionLine = reader.readLine();
                if (instructionLine == null) {
                    System.err.println("Warning: Incomplete input for rover at: " + positionLine);
                    break;
                }


                Rover rover = parseRover(positionLine, plateau);
                List<Command> commands = CommandParser.parseAll(instructionLine);


                for (Command command : commands) {
                    command.execute(rover);
                }


                System.out.println(rover.formattedPosition());
            }

        } catch (IOException e) {
            System.err.println("Failed to read input file: " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Simulation error: " + e.getMessage());
        }
    }

    private static Plateau parsePlateau(String line) {
        String[] parts = line.trim().split("\\s+");
        int maxX = Integer.parseInt(parts[0]);
        int maxY = Integer.parseInt(parts[1]);
        return new Plateau(maxX, maxY);
    }

    private static Rover parseRover(String line, Plateau plateau) {
        String[] parts = line.trim().split("\\s+");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        Direction direction = Direction.valueOf(parts[2].toUpperCase());
        return new Rover(new Coordinates(x, y), direction, plateau);
    }
}