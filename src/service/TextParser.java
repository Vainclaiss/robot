package service;

import Entities.Plateau;
import Entities.RectanglePlateau;
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
        // VerifyAndSplit va séparer en plusieur part, si (on n'a pas 2 il refuse, sinon il passe)
        String[] parts = verifyAndSplit(line, 2, 1, "maxX maxY");
        try {

            // On verifie si c'est bien des nombres (il n'y a pas de lettres)
            int maxX = Integer.parseInt(parts[0]);
            int maxY = Integer.parseInt(parts[1]);

            // Pour l'instant directement RectanglePlateau mais sinon un Factory de plateau, si on peut avoir rectangulaire..
            return new RectanglePlateau(maxX, maxY);
        } catch (IllegalArgumentException e) { //
            throw new InvalidInputException("Line 1: invalid plateau '" + line + "'");
        }
    }
    // TODO: Est ce qu'on fait la vérification que le Rover respecte les dimensions du Plateau ? Ou est ce qu'on sort du domaine du textParser?
    // S'assurer que les lettres sont OK (A-Z) -> fait par Integer.parseInt (sinon va throw un message d'erreur)
    private RoverSpec parseRover(String positionLine, String instructionLine, int lineNo) {
        String[] parts = verifyAndSplit(positionLine, 3, lineNo, "x y initialDirection");
        List<Instruction> instructions = parseInstructions(instructionLine, lineNo + 1);

        try {
            Coordinates startingPosition = new Coordinates(
                    Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            Direction initialDirection = Direction.valueOf(parts[2]);

            return new RoverSpec(startingPosition, initialDirection, instructions);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Line " + lineNo + ": " + e.getMessage());
        }
    }

    // Inside TextParser.java
    private List<Instruction> parseInstructions(String line, int lineNo) {
        List<Instruction> result = new ArrayList<>();
        for (char c : line.strip().toCharArray()) {
            try {
                // La verification est ici si l'instruction ne fait pas partie de L ,R ou M elle est skip par Instruction.valueOf()
                result.add(Instruction.valueOf(String.valueOf(c)));
            } catch (IllegalArgumentException e) {
                throw new InvalidInputException("Line " + lineNo + ": unknown instruction '" + c + "'");
            }
        }
        return result;
    }
    // A refaire verifyandSplit
    private String[] verifyAndSplit(String line, int expected, int lineNo, String format) {
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