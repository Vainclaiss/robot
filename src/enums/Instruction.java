package enums;

public enum Instruction {
    L, R, M;

    // à la place de from il y'a une méthode qui fait ça

    public static Instruction from(char c) {
        return switch (c) {
            case 'L' -> L;
            case 'R' -> R;
            case 'M' -> M;
            default -> throw new IllegalArgumentException("Unknown instruction '" + c + "'");
        };
    }
}
