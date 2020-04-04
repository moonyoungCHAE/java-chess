package chess.domain.game;

import chess.Exception.IllegalMoveException;
import chess.domain.chesspiece.King;
import chess.domain.chesspiece.Pawn;
import chess.domain.chesspiece.Piece;
import chess.domain.chesspiece.PieceInfo;
import chess.domain.direction.Direction;
import chess.domain.position.Position;
import chess.domain.position.component.Row;
import chess.domain.status.Result;
import chess.domain.status.Status;

import java.util.*;
import java.util.stream.Collectors;

public class ChessBoard {
    private final Map<Position, Piece> chessBoard = new HashMap<>();
    private boolean isKingTaken;

    public ChessBoard(Map<Piece, List<Position>> initPieces) {
        for (Map.Entry<Piece, List<Position>> entry : initPieces.entrySet()) {
            entry.getValue().forEach(position -> chessBoard.put(position, entry.getKey()));
        }
        isKingTaken = false;
    }

    public boolean move(Position from, Position to) {
        Piece source = chessBoard.get(from);
        Piece target = chessBoard.get(to);
        validateSamePosition(from, to);
        validateSource(source);
        validateIsPlayer(source, target);


        if (movable(from, to)) {
            chessBoard.put(to, source);
            chessBoard.remove(from);

            if (target instanceof King) {
                this.isKingTaken = true;
            }
            return true;
        }
        return false;
    }

    private void validateSamePosition(Position from, Position to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        if (from == to) {
            throw new IllegalMoveException("같은 위치로 이동할 수 없습니다.");
        }
    }

    private void validateSource(Piece source) {
        if (Objects.isNull(source)) {
            throw new IllegalMoveException("empty에서는 이동할 수 없습니다.");
        }
    }

    private void validateIsPlayer(Piece source, Piece target) {
        if (Objects.nonNull(target) && source.isSamePlayer(target)) {
            throw new IllegalMoveException("같은 Player의 기물로는 이동할 수 없습니다.");
        }
    }

    private boolean movable(Position from, Position to) {
        Piece source = chessBoard.get(from);
        Piece target = chessBoard.get(to);
        Direction direction = Direction.getDirection(from, to);

        if (source.needValidateObstacle()) {
            return source.validateTileSize(from, to)
                    && source.validateDirection(direction, target)
                    && validateObstacles(getRoutes(from, to));
        }
        return source.validateTileSize(from, to)
                && source.validateDirection(direction, target);
    }

    public boolean isGameOver() {
        return isKingTaken;
    }

    private boolean validateObstacles(List<Position> routes) {
        for (Position  position : routes) {
            if (Objects.nonNull(chessBoard.get(position))) {
                return false;
            }
        }
        return true;
    }

    public boolean validateTurn(Position position, Player player) {
        Piece piece = chessBoard.get(position);
        if (Objects.isNull(piece)) {
            return false;
        }
        return piece.isPlayer(player);
    }

    public Map<Position, Piece> getChessBoard() {
        return Collections.unmodifiableMap(chessBoard);
    }

    private List<Position> getRoutes(Position from, Position to) {
        Direction direction = Direction.getDirection(from, to);
        return direction.getPositionsBetween(from, to);
    }

    public Status createStatus(Player player) {
        if (checkKingTaken(player)) {
            return new Status(player, 0);
        }

        double score = getPlayerPieces(player)
                .stream()
                .mapToDouble(Piece::getScore)
                .sum();
        score -= PieceInfo.PAWN_SCORE_DIFF * getPawnCount(player);
        return new Status(player, score);
    }

    public boolean checkKingTaken(Player player) {
        int kingCount = (int) getPlayerPieces(player)
                .stream()
                .filter(piece -> piece instanceof  King)
                .count();
        return kingCount > 0;
    }
    public Result createResult() {
        List<Status> statuses = Arrays.asList(createStatus(Player.WHITE), createStatus(Player.BLACK));
        return new Result(statuses);
    }

    private List<Piece> getPlayerPieces(Player player) {
        return chessBoard.values()
                .stream()
                .filter(piece -> piece.getPlayer() == player)
                .collect(Collectors.toList());
    }

    public int getPawnCountPerStage(List<Piece> columnLine, Player player) {
        return (int) columnLine.stream()
                .filter(piece -> piece instanceof Pawn)
                .filter(pawn -> pawn.getPlayer() == player)
                .count();
    }

    public int getPawnCount(Player player) {
        int count = 0;
        for (Row row : Row.values()) {
            int value = getPawnCountPerStage(getStage(row), player);
            if (value != 1) {
                count += value;
            }
        }
        return count;
    }

    public List<Piece> getStage(Row row) {
        List<Piece> squares = new ArrayList<>();
        for (Map.Entry<Position, Piece> entry : chessBoard.entrySet()) {
            if (entry.getKey().getRow() == row)
                squares.add(entry.getValue());
        }
        return squares;
    }
}
