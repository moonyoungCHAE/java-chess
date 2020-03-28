package chess.domain.routes;

import chess.domain.direction.Direction;
import chess.domain.position.Position;

import java.util.ArrayList;
import java.util.List;

public class RoutesFinder {
    private static final DiagonalRoutes diagonalRoutes = new DiagonalRoutes();
    private static final LeftRightRoutes leftRightRoutes = new LeftRightRoutes();
    private static final TopDownRoutes topDownRoutes = new TopDownRoutes();

    public static List<Position> findRoutes(Direction direction, Position from, Position to) {
        if (diagonalRoutes.hasDirection(direction)) {
            return diagonalRoutes.findRoutes(from, to);
        }

        if (leftRightRoutes.hasDirection(direction)) {
            return leftRightRoutes.findRoutes(from, to);
        }

        if (topDownRoutes.hasDirection(direction)) {
            return topDownRoutes.findRoutes(from, to);
        }

        return new ArrayList<>();
    }
}
