import chess.domain.chesspiece.concrete.King;
import chess.domain.chesspiece.concrete.Pawn;
import chess.domain.chesspiece.Piece;
import chess.domain.chesspiece.concrete.Queen;
import chess.domain.game.ChessBoard;
import chess.domain.game.ChessBoardFactory;
import chess.domain.game.Player;
import chess.domain.position.Position;
import chess.domain.position.Positions;
import chess.domain.result.Result;
import chess.domain.result.Score;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ChessBoardTest {
    @DisplayName("초기 체스판 개수 확인")
    @Test
    void initChessBoard() {
        ChessBoard chessBoard = ChessBoardFactory.create();
        int actual = chessBoard.getChessBoard().size();
        int expected = 32;
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("(예외) 같은 위치로 이동")
    @Test
    void moveSamePosition() {
        ChessBoard chessBoard = ChessBoardFactory.create();
        Position position = Positions.of("a1");
        assertThatThrownBy(() -> chessBoard.move(position, position))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("점수 계산")
    @ParameterizedTest
    @EnumSource(value = Player.class)
    void createStatusTest(Player player) {
        ChessBoard chessBoard = ChessBoardFactory.create();
        Score result = chessBoard.createStatus(player);
        double actual = result.getScore();
        double expected = 38;
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("ChessBoard 한 column에 같은 Player 소유의 Pawn 개수 확인")
    @ParameterizedTest
    @MethodSource("generatePositions3")
    void existPawnNumberTest(List<Piece> columnLine, Player player, int exptectd) {
        ChessBoard chessBoard = ChessBoardFactory.create();
        assertThat(chessBoard.getPawnCountPerStage(columnLine, player)).isEqualTo(exptectd);
    }

    static Stream<Arguments> generatePositions3() {
        List<Piece> whitePawnWhiteKing = Arrays.asList(new Pawn(Player.WHITE),
                new King(Player.WHITE));
        List<Piece> whitePawn3 = Arrays.asList(new Pawn(Player.WHITE),
                new Pawn(Player.WHITE),
                new Pawn(Player.WHITE)
        );
        List<Piece> whitePawn1BlackPawn2 = Arrays.asList(new Pawn(Player.WHITE),
                new Pawn(Player.BLACK),
                new Pawn(Player.BLACK));
        List<Piece> whiteKingBlackQueen = Arrays.asList(new King(Player.WHITE), new Queen(Player.BLACK));

        return Stream.of(Arguments.of(whitePawnWhiteKing, Player.WHITE, 1),
                Arguments.of(whitePawn3, Player.WHITE, 3),
                Arguments.of(whitePawn1BlackPawn2, Player.WHITE, 1),
                Arguments.of(whiteKingBlackQueen, Player.WHITE, 0));
    }

    @DisplayName("우승자 확인")
    @Test
    void 우승자_확인() {
        List<Score> scores = new ArrayList<>();
        scores.add(new Score(Player.WHITE, 10));
        scores.add(new Score(Player.BLACK, 20));

        Result result = new Result(scores);
        assertThat(result.getWinners()).isEqualTo(Arrays.asList(Player.BLACK));
    }
}