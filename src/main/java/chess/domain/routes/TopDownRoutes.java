package chess.domain.routes;

import chess.domain.direction.Direction;
import chess.domain.position.Position;
import chess.domain.position.Positions;
import chess.domain.position.component.Column;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TopDownRoutes implements Routes {
    private static final List<Direction> directions = Arrays.asList(Direction.TOP, Direction.DOWN);

    @Override
    public List<Position> findRoutes(Position from, Position to) {
        Column smallerColumn = Column.getSmaller(from.getColumn(), to.getColumn());
        Column biggerColumn = Column.getBigger(from.getColumn(), to.getColumn());
        List<Column> columns = Arrays.asList(Column.values())
                .subList(smallerColumn.ordinal() + 1, biggerColumn.ordinal());
        return columns.stream()
                .map(column -> Positions.of(from.getRow(), column))
                .collect(Collectors.toList());
    }


    @Override
    public boolean hasDirection(Direction direction) {
        return directions.contains(direction);
    }
}
