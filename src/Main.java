import exceptions.InvalidInputException;
import records.Mission;
import service.MissionExecutor;
import Entities.Rover;
import service.TextParser;

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

            // Pas besoin de fermer le fichier, files.readAllLines ouvre le fichier, lit toutes les lignes et ferme le fichier.
            List<String> lines = Files.readAllLines(Path.of(args[0]));
            Mission mission = new TextParser().parse(lines);
            List<Rover> rovers = new MissionExecutor(mission).startMission();
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