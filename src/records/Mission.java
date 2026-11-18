package records;

import Entities.Plateau;

import java.util.List;

public record Mission(Plateau plateau, List<RoverSpec> rovers) {}