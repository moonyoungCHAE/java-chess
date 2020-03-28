package chess.domain.routes;

import chess.domain.direction.Direction;
import chess.domain.position.Position;

import java.util.List;

public interface Routes {
    List<Position> findRoutes(Position from, Position to);
    boolean hasDirection(Direction direction);
}
