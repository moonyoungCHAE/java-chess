import chess.Status;
import chess.domain.ChessBoard;
import chess.domain.Player;
import chess.domain.chesspieces.*;
import chess.domain.position.Position;
import chess.domain.position.Positions;
import chess.domain.position.component.Column;
import chess.domain.position.component.Row;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ChessBoardTest {
    @DisplayName("초기 체스판 개수 확인")
    @Test
    void initChessBoard() {
        ChessBoard chessBoard = new ChessBoard();
        int actual = chessBoard.getChessBoard().size();
        int expected = Column.values().length * Row.values().length;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 같은_위치로_이동했을때() {
        ChessBoard chessBoard = new ChessBoard();
        Position position = Positions.of("a1");
        assertThatThrownBy(() -> chessBoard.move(position, position))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("각각의_플레이어_점수_계산_테스트")
    @ParameterizedTest
    @EnumSource(value = Player.class)
    void computeScoreTest(Player player) {
        ChessBoard chessBoard = new ChessBoard();
        Status result = chessBoard.createStatus(player);
        double actual = result.getScore();
        double expected = 38;
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("ChessBoard의 한 column에 같은 Player 소유의 Pawn 개수 확인")
    @ParameterizedTest
    @MethodSource("generatePositions3")
    void 폰의점수를확인하는테스트(List<Square> columnLine, Player player, int exptectd) {
        ChessBoard chessBoard = new ChessBoard();
        assertThat(chessBoard.getPawnCountPerStage(columnLine, player)).isEqualTo(exptectd);
    }

    static Stream<Arguments> generatePositions3() {
        Stream<Piece> whitePawnWhiteKing = Stream.of(new Pawn(Player.WHITE, Positions.of("a1")),
                new King(Player.WHITE));
        Stream<Piece> whitePawn3 = Stream.of(new Pawn(Player.WHITE, Positions.of("a1")),
                new Pawn(Player.WHITE, Positions.of("a2")),
                new Pawn(Player.WHITE, Positions.of("a3"))
        );
        Stream<Piece> whitePawn1BlackPawn2 = Stream.of(new Pawn(Player.WHITE, Positions.of("a1")),
                new Pawn(Player.BLACK, Positions.of("a2")),
                new Pawn(Player.BLACK, Positions.of("a3")));
        Stream<Piece> whiteKingBlackQueen = Stream.of(new King(Player.WHITE), new Queen(Player.BLACK));

        List<Empty> emptyStream_Size5 = Stream.generate(Empty::getInstance)
                .limit(5).collect(Collectors.toList());
        List<Empty> emptyStream_Size6 = Stream.generate(Empty::getInstance)
                .limit(6).collect(Collectors.toList());
        return Stream.of(
                Arguments.of(
                        Stream.concat(whitePawnWhiteKing, emptyStream_Size6.stream())
                                .collect(Collectors.toList()), Player.WHITE, 1
                ), Arguments.of(
                        Stream.concat(whitePawn3, emptyStream_Size5.stream())
                                .collect(Collectors.toList()), Player.WHITE, 3
                ), Arguments.of(
                        Stream.concat(whitePawn1BlackPawn2, emptyStream_Size5.stream())
                                .collect(Collectors.toList()), Player.WHITE, 1
                ), Arguments.of(
                        Stream.concat(whiteKingBlackQueen, emptyStream_Size6.stream())
                                .collect(Collectors.toList()), Player.WHITE, 0
                )
        );
    }

    @Test
    void 우승자_확인() {

    }
}