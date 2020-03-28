package chess.domain.chesspieces;

import chess.domain.Player;
import chess.domain.direction.Direction;
import chess.domain.position.Position;

import java.util.Arrays;

public class Knight extends Piece {
    private static final String KNIGHT_NAME = "KNIGHT";

    private static PieceInfo pieceInfo = PieceInfo.valueOf(KNIGHT_NAME);

    public Knight(Player player) {
        super(player, pieceInfo);
        directions.addAll(Arrays.asList(Direction.values()));
    }

    @Override
    public boolean movable(Position from, Position to) {
        return super.movable(from, to) && validateMovePosition(from, to);
    }

    public boolean validateMovePosition(Position from, Position to) {
        return Arrays.stream(MovePosition.values())
                .anyMatch(movePosition -> movePosition.contains(from, to));
    }

    @Override
    public boolean validateMovableTileSize(Position from, Position to) {
        return false;
    }

    private enum MovePosition {
        NORTH_EAST(1, 2),
        NORTH_WEST(-1, 2),
        EAST_NORTH(2, 1),
        EAST_SOUTH(2, -1),
        SOUTH_EAST(1, -2),
        SOUTH_WEST(-1, -2),
        WEST_SOUTH(-2, -1),
        WEST_NORTH(-2, 1);

        private int rowDiff;
        private int columnDiff;

        MovePosition(int rowDiff, int columnDiff) {
            this.rowDiff = rowDiff;
            this.columnDiff = columnDiff;
        }

        boolean contains(Position source, Position target) {
            return (source.getRow().ordinal() + rowDiff == target.getRow().ordinal())
                    && (source.getColumn().ordinal() + columnDiff == target.getColumn().ordinal());
        }
    }
}
