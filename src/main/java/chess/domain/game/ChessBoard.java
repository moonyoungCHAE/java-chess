package chess.domain.game;

import chess.domain.chesspiece.concrete.King;
import chess.domain.chesspiece.concrete.Pawn;
import chess.domain.chesspiece.Piece;
import chess.domain.chesspiece.PieceInfo;
import chess.domain.direction.Direction;
import chess.domain.position.Position;
import chess.domain.position.component.Row;
import chess.domain.result.Result;
import chess.domain.result.Score;

import java.util.*;
import java.util.stream.Collectors;

public class ChessBoard {
    private final Map<Position, Piece> chessBoard;
//    private boolean isKingTaken;

    public ChessBoard(Map<Position, Piece> initPieces) {
        this.chessBoard = initPieces;
//        isKingTaken = false;
    }

    public void move(Position from, Position to) {
        Piece source = chessBoard.get(from);
        Piece target = chessBoard.get(to);
        validateSamePosition(from, to);
        validateSource(source);
        validateIsPlayer(source, target);

        if (!movable(from, to)) {
            throw new IllegalArgumentException("이동할 수 없습니다.");
        }

        chessBoard.put(to, source);
        chessBoard.remove(from);

//        if (target instanceof King) {
//            this.isKingTaken = true;
//        }
    }

    private void validateSamePosition(Position from, Position to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        if (from == to) {
            throw new IllegalArgumentException("같은 위치로 이동할 수 없습니다.");
        }
    }

    private void validateSource(Piece source) {
        if (Objects.isNull(source)) {
            throw new IllegalArgumentException("empty에서는 이동할 수 없습니다.");
        }
    }

    private void validateIsPlayer(Piece source, Piece target) {
        if (Objects.nonNull(target) && source.isSamePlayer(target)) {
            throw new IllegalArgumentException("같은 Player의 기물로는 이동할 수 없습니다.");
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

//    public boolean isGameFinished() {
//        return isKingTaken;
//    }

    private boolean validateObstacles(List<Position> routes) {
        for (Position position : routes) {
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

    public Score createStatus(Player player) {
        if (checkKingTaken(player)) {
            return new Score(player, 0);
        }

        double score = getPlayerPieces(player)
                .stream()
                .mapToDouble(Piece::getScore)
                .sum();
        score -= PieceInfo.PAWN_SCORE_DIFF * getPawnCount(player);
        return new Score(player, score);
    }

    public boolean checkKingTaken(Player player) {
        int kingCount = (int) getPlayerPieces(player)
                .stream()
                .filter(piece -> piece instanceof King)
                .count();
        return kingCount == 0;
    }

    public Result createResult() {
        List<Score> scores = Arrays.asList(createStatus(Player.WHITE), createStatus(Player.BLACK));
        return new Result(scores);
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
