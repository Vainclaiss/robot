package service;

import Entities.Rover;
import enums.Instruction;
import records.Mission;
import records.RoverSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MissionExecutor {

    private final Mission mission;

    public MissionExecutor(Mission mission) {
        this.mission = Objects.requireNonNull(mission, "Mission must be non null");
    }

    // Je considere que deux rovers peuvent exister sur la même case
    public List<Rover> startMission() {
        List<Rover> deployed = new ArrayList<>();
        for (RoverSpec spec : mission.rovers()) {

            if(!mission.plateau().isWithinBounds(spec.startingPosition()))
            {
                throw new IllegalStateException("Rover cannot be instantiated" + spec.startingPosition() + " is outside the plateau");
            }

            Rover rover = new Rover(spec.startingPosition(), spec.direction());
            executeRoverInstructions(rover, spec.instructions());
            deployed.add(rover);
        }
        return List.copyOf(deployed);
    }

    private void executeRoverInstructions(Rover rover, List<Instruction> instructions) {
        for (Instruction instruction : instructions) {
            switch (instruction) {
                case L -> rover.turnLeft();
                case R -> rover.turnRight();
                case M -> {

                    // If it is not inside the bounds we just skip that instruction.
                    if (mission.plateau().isWithinBounds(rover.nextPosition())) {
                        rover.move();
                    }
                }
            }
        }
    }


    }
