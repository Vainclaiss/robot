package service;

import enums.Instruction;
import records.Coordinates;
import records.Mission;
import records.RoverSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MissionExecutor {

    private final Mission mission;

    public MissionExecutor(Mission mission) {
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
        if (!mission.plateau().isWithinBounds(spec.startingPosition())) {
            throw new IllegalArgumentException(
                    "Initial position " + spec.startingPosition() + " is outside the plateau");
        }
        return new Rover(spec.startingPosition(), spec.direction());
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