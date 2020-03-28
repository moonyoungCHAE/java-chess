package chess.domain.routes;

import chess.domain.direction.Direction;
import chess.domain.position.Position;
import chess.domain.position.Positions;
import chess.domain.position.component.Row;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LeftRightRoutes implements Routes{
    private static final List<Direction> directions = Arrays.asList(Direction.LEFT, Direction.RIGHT);

    @Override
    public List<Position> findRoutes(Position from, Position to) {
        System.out.println("come left right");
        Row smallerRow = Row.getSmaller(from.getRow(), to.getRow());
        Row biggerRow = Row.getBigger(from.getRow(), to.getRow());
        List<Row> rows = Arrays.asList(Row.values())
                .subList(smallerRow.ordinal() + 1, biggerRow.ordinal());
        return rows.stream()
                .map(row -> Positions.of(row, from.getColumn()))
                .collect(Collectors.toList());

    }

    @Override
    public boolean hasDirection(Direction direction) {
        return directions.contains(direction);
    }
}
