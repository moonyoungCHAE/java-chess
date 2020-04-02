package chess.domain.chesspiece;

import chess.domain.direction.Direction;
import chess.domain.game.Player;
import chess.domain.position.Position;
import chess.domain.position.component.Column;
import chess.domain.position.component.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Pawn extends Piece {
    private static final String PAWN_NAME = "PAWN";

    private final List<Direction> attackDirections = new ArrayList<>();
    private final Position initPosition;

    private Direction forwardDirection;

    public Pawn(Player player, Position position) {
        super(player, PieceInfo.valueOf(PAWN_NAME));
        this.initPosition = position;

        if (player.equals(Player.BLACK)) {
            forwardDirection = Direction.SOUTH;
            attackDirections.addAll(Arrays.asList(Direction.SOUTH_WEST, Direction.SOUTH_EAST));
            directions.add(forwardDirection);
            directions.addAll(attackDirections);
        }

        if (player.equals(Player.WHITE)) {
            forwardDirection = Direction.NORTH;
            attackDirections.addAll(Arrays.asList(Direction.NORTH_WEST, Direction.NORTH_EAST));
            directions.add(forwardDirection);
            directions.addAll(attackDirections);
        }
    }

    @Override
    public boolean validateTileSize(Position from, Position to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        int rowDiff = Row.getDiff(from.getRow(), to.getRow());
        int columnDiff = Column.getDiff(from.getColumn(), to.getColumn());

        int movableColumnDiff = pieceInfo.getMovableColumnDiff();
        if (initPosition == from) {
            movableColumnDiff = PieceInfo.PAWN_INIT_MOVABLE_COLUMN_DIFF;
        }
        return Math.abs(rowDiff) <= pieceInfo.getMovableRowDiff() && Math.abs(columnDiff) <= movableColumnDiff;
    }

    @Override
    public boolean validateDirection(Direction direction, Piece target) {
        return hasDirection(direction)
                && (validateMoveAttack(direction, target) || validateMoveForward(direction, target));
    }

    public boolean validateMoveAttack(Direction direction, Piece target) {
        Objects.requireNonNull(direction);
        boolean isNotEmpty = Objects.nonNull(target);
        return attackDirections.contains(direction)
                && isNotEmpty
                && !(this.isSamePlayer(target));
    }

    public boolean validateMoveForward(Direction direction, Piece target) {
        Objects.requireNonNull(direction);
        boolean isEmpty = target == null;
        return direction == forwardDirection && isEmpty;
    }
}
