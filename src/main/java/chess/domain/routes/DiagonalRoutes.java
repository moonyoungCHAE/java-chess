package chess.domain.routes;

import chess.domain.direction.Direction;
import chess.domain.position.Position;
import chess.domain.position.Positions;
import chess.domain.position.component.Column;
import chess.domain.position.component.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiagonalRoutes implements Routes {
    private static final List<Direction> directions = Arrays.asList(Direction.DIAGONAL_TOP_RIGHT,
            Direction.DIAGONAL_TOP_LEFT,
            Direction.DIAGONAL_DOWN_RIGHT,
            Direction.DIAGONAL_DOWN_LEFT);

    @Override
    public List<Position> findRoutes(Position from, Position to) {
        Column smallerColumn = Column.getSmaller(from.getColumn(), to.getColumn());
        Column biggerColumn = Column.getBigger(from.getColumn(), to.getColumn());
        Row smallerRow = Row.getSmaller(from.getRow(), to.getRow());
        Row biggerRow = Row.getBigger(from.getRow(), to.getRow());

        List<Row> rows = Arrays.asList(Row.values())
                .subList(smallerRow.ordinal() + 1, biggerRow.ordinal());
        List<Column> columns = Arrays.asList(Column.values())
                .subList(smallerColumn.ordinal() + 1, biggerColumn.ordinal());
        List<Position> positions = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            positions.add(Positions.of(rows.get(i), columns.get(i)));
        }
        return positions;
    }

    @Override
    public boolean hasDirection(Direction direction) {
        return directions.contains(direction);
    }
}
