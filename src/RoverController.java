import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class RoverController {

    private final Mission mission;

    public RoverController(Mission mission) {
        this.mission = Objects.requireNonNull(mission, "Mission must not be null");
    }


    public List<Rover> run() {
        List<Rover> results = new ArrayList<>();
        for (RoverSpec spec : mission.rovers()) {
            Rover rover = deploy(spec);
            execute(rover, spec.instructions());
            results.add(rover);
        }
        return results;
    }

    private Rover deploy(RoverSpec spec) {
        if (!mission.plateau().isWithinBounds(spec.start())) {
            throw new IllegalArgumentException(
                    "Initial position " + spec.start() + " is outside the plateau");
        }
        return new Rover(spec.start(), spec.heading());
    }

    private void execute(Rover rover, List<Instruction> instructions) {
        for (Instruction instruction : instructions) {
            switch (instruction) {
                case L -> rover.turnLeft();
                case R -> rover.turnRight();
                case M -> move(rover);
            }
        }
    }

    private void move(Rover rover) {
        Coordinates next = rover.nextPosition();
        if (!mission.plateau().isWithinBounds(next)) {
            throw new IllegalStateException("Move to " + next + " leaves the plateau");
        }
        rover.moveTo(next);
    }
}