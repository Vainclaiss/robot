package records;

public record Coordinates(int x , int y) {

    public Coordinates move (int dx, int dy) {
        return new Coordinates(this.x + dx, this.y + dy);

    }
}
