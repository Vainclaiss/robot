import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar rover.jar <input-file>");
            System.exit(1);
        }

        try {
            List<String> lines = Files.readAllLines(Path.of(args[0]));
            Mission mission = new TextParser().parse(lines);
            List<Rover> rovers = new RoverController(mission).run();
            rovers.forEach(System.out::println);
        } catch (IOException e) {
            fail("Cannot read file: " + e.getMessage());
        } catch (InvalidInputException | IllegalArgumentException | IllegalStateException e) {
            fail(e.getMessage());
        }
    }

    private static void fail(String message) {
        System.err.println("Error: " + message);
        System.exit(1);
    }
}