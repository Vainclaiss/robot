package service;

import enums.Direction;
import enums.Instruction;
import exceptions.InvalidInputException;
import records.Coordinates;
import records.Mission;
import records.RoverSpec;

import java.util.ArrayList;
import java.util.List;

public final class TextParser {

    public Mission parse(List<String> rawLines) {
        List<String> lines = withoutTrailingBlanks(rawLines);
        if (lines.isEmpty()) {
            throw new InvalidInputException("Input is empty");
        }
        if (lines.size() % 2 == 0) {
            throw new InvalidInputException(
                    "Each rover needs a position line followed by an instruction line");
        }

        Plateau plateau = parsePlateau(lines.get(0));
        List<RoverSpec> rovers = new ArrayList<>();
        for (int i = 1; i < lines.size(); i += 2) {
            int lineNo = i + 1;
            rovers.add(parseRover(lines.get(i), lines.get(i + 1), lineNo));
        }
        return new Mission(plateau, rovers);
    }

    private Plateau parsePlateau(String line) {
        String[] parts = split(line, 2, 1, "maxX maxY");
        try {
            // Pour l'instant directement service.RectanglePlateau mais sinon un Factory de plateau, si on peut avoir rectangulaire..
            // S'assurer qu'il y'a pas de lettre
            return new RectanglePlateau(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        } catch (IllegalArgumentException e) { //
            throw new InvalidInputException("Line 1: invalid plateau '" + line + "'");
        }
    }
    // Fait tous les vérifications, s'assure que le Rover est dans une case correcte. (pas dans MissionExecutor)
    // S'assurer que les lettres sont OK (A-Z)
    private RoverSpec parseRover(String positionLine, String instructionLine, int lineNo) {
        String[] parts = split(positionLine, 3, lineNo, "x y initialDirection");
        try {
            Coordinates startingPosition = new Coordinates(
                    Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            Direction initialDirection = Direction.valueOf(parts[2]);
            return new RoverSpec(startingPosition, initialDirection, parseInstructions(instructionLine, lineNo + 1));
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(
                    "Line " + lineNo + ": invalid rover '" + positionLine + "'");
        }
    }

    private List<Instruction> parseInstructions(String line, int lineNo) {
        // verifier les lettres, ya pas de chiffres
        List<Instruction> result = new ArrayList<>();
        for (char c : line.strip().toCharArray()) {
            try {
                result.add(Instruction.from(c)); // L , R , M
            } catch (IllegalArgumentException e) {
                throw new InvalidInputException("Line " + lineNo + ": " + e.getMessage());
            }
        }
        return result;
    }
    // A refaire verifyandSplit -- on peut le mettre dans split()
    private String[] split(String line, int expected, int lineNo, String format) {
        String[] parts = line.strip().split("\\s+");
        if (parts.length != expected) {
            throw new InvalidInputException(
                    "Line " + lineNo + ": expected '" + format + "' but got '" + line + "'");

        }
        return parts;
    }

    // Utiliser les méthodes prédifinies de StringBuffer ou String

    private List<String> withoutTrailingBlanks(List<String> lines) {
        int end = lines.size();
        while (end > 0 && lines.get(end - 1).isBlank()) end--;
        return lines.subList(0, end);
    }
}