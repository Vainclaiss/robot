package enums;

public enum Instruction {
    L, R, M;

    public static Instruction from(char c) {
        return switch (c) {
            case 'L' -> L;
            case 'R' -> R;
            case 'M' -> M;
            default -> throw new IllegalArgumentException("Unknown instruction '" + c + "'");
        };
    }
}
