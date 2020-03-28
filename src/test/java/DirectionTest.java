import chess.domain.moverules.Direction;
import chess.domain.position.Position;
import chess.domain.position.Positions;
import chess.domain.position.component.Column;
import chess.domain.position.component.Row;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectionTest {

    @ParameterizedTest
    @MethodSource("generatePositionsAndMoveRule")
    void 방향찾기테스트(String sourceInput, String targetInput, Direction direction) {
        Position source = Positions.of(sourceInput);
        Position target = Positions.of(targetInput);

        assertThat(Direction.getMoveRule(source, target)).isEqualTo(direction);
    }

    static Stream<Arguments> generatePositionsAndMoveRule() {
        return Stream.of(
                Arguments.of("a1", "a8", Direction.TOP),
                Arguments.of("a8", "a1", Direction.DOWN),
                Arguments.of("a1", "h1", Direction.RIGHT),
                Arguments.of("h1", "a1", Direction.LEFT),
                Arguments.of("a8", "h1", Direction.DIAGONAL_DOWN_RIGHT),
                Arguments.of("h8", "a1", Direction.DIAGONAL_DOWN_LEFT),
                Arguments.of("h1", "a8", Direction.DIAGONAL_TOP_LEFT),
                Arguments.of("a1", "h8", Direction.DIAGONAL_TOP_RIGHT));
    }

    @DisplayName( "top/down 이동했을 경우 source와 target 사이의 position들 확인")
    @ParameterizedTest
    @MethodSource("generateTopDownPositions")
    void opDownTest(String sourceInput, String targetValue, Direction direction) {
        Position source = Positions.of(sourceInput);
        Position target = Positions.of(targetValue);

        List<Position> actual = direction.getPositionsBetween(source, target);

        Position[] expected = {Positions.of(Row.A, Column.TWO),
                Positions.of(Row.A, Column.THREE),
                Positions.of(Row.A, Column.FOUR),
                Positions.of(Row.A, Column.FIVE),
                Positions.of(Row.A, Column.SIX),
                Positions.of(Row.A, Column.SEVEN)};

        assertThat(actual).containsExactly(expected);
    }

    static Stream<Arguments> generateTopDownPositions() {
        return Stream.of(
                Arguments.of("a1", "a8", Direction.TOP),
                Arguments.of("a8", "a1", Direction.DOWN));
    }

    @DisplayName("Left/Right 이동했을 경우 source와 target 사이의 position들 확인")
    @ParameterizedTest
    @MethodSource("generateLeftRightPositions")
    void leftRightTest(String sourceInput, String targetValue, Direction direction) {
        Position source = Positions.of(sourceInput);
        Position target = Positions.of(targetValue);

        List<Position> actual = direction.getPositionsBetween(source, target);

        Position[] expected = {Positions.of(Row.B, Column.ONE),
                Positions.of(Row.C, Column.ONE),
                Positions.of(Row.D, Column.ONE),
                Positions.of(Row.E, Column.ONE),
                Positions.of(Row.F, Column.ONE),
                Positions.of(Row.G, Column.ONE)};

        assertThat(actual).containsExactly(expected);
    }

    static Stream<Arguments> generateLeftRightPositions() {
        return Stream.of(
                Arguments.of("a1", "h1", Direction.LEFT),
                Arguments.of("h1", "a1", Direction.RIGHT));
    }

    @DisplayName("왼쪽 아래, 오른쪽 위 이동했을 경우 source와 target 사이의 position들 확인")
    @ParameterizedTest
    @MethodSource("generateTopLeftDownRightPositions")
    void diagonalTopLeftDownRightTest(String sourceInput, String targetValue, Direction direction) {
        Position source = Positions.of(sourceInput);
        Position target = Positions.of(targetValue);

        List<Position> actual = direction.getPositionsBetween(source, target);

        Position[] expected = {Positions.of(Row.B, Column.TWO),
                Positions.of(Row.C, Column.THREE),
                Positions.of(Row.D, Column.FOUR),
                Positions.of(Row.E, Column.FIVE),
                Positions.of(Row.F, Column.SIX),
                Positions.of(Row.G, Column.SEVEN)};

        assertThat(actual).containsExactly(expected);
    }

    static Stream<Arguments> generateTopLeftDownRightPositions() {
        return Stream.of(
                Arguments.of("a1", "h8", Direction.DIAGONAL_TOP_RIGHT),
                Arguments.of("h8", "a1", Direction.DIAGONAL_DOWN_LEFT));
    }

    @DisplayName("왼쪽 위, 오른쪽 아래 이동했을 경우 source와 target 사이의 position들 확인")
    @ParameterizedTest
    @MethodSource("generateTopRightDownLeftPositions")
    void diagonalTopRightDownLeftTest(String sourceInput, String targetValue, Direction direction) {
        Position source = Positions.of(sourceInput);
        Position target = Positions.of(targetValue);

        List<Position> actual = direction.getPositionsBetween(source, target);

        Position[] expected = {Positions.of(Row.B, Column.TWO),
                Positions.of(Row.C, Column.THREE),
                Positions.of(Row.D, Column.FOUR),
                Positions.of(Row.E, Column.FIVE),
                Positions.of(Row.F, Column.SIX),
                Positions.of(Row.G, Column.SEVEN)};

        assertThat(actual).containsExactly(expected);
    }

    static Stream<Arguments> generateTopRightDownLeftPositions() {
        return Stream.of(
                Arguments.of("h1", "a8", Direction.DIAGONAL_TOP_LEFT),
                Arguments.of("a8", "h1", Direction.DIAGONAL_DOWN_RIGHT));
    }
}
