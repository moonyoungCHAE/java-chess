package chess.domain.game;

import chess.Exception.NotStartPlayingException;
import chess.controller.dto.RequestDto;
import chess.controller.dto.ResponseDto;

public class ChessGame {
    private Player turn;
    private ChessBoard chessBoard;

    public ChessGame() {
        this.turn = Player.WHITE;
        this.chessBoard = new ChessBoard();
    }

    public ResponseDto start(RequestDto requestDto) {
        Command command = requestDto.getCommend();

        if (command != Command.START) {
            throw new NotStartPlayingException();
        }

        chessBoard.create(PieceFactory.create());

        return new ResponseDto(chessBoard, chessBoard.createResult());
    }

    public ResponseDto move(RequestDto requestDto) {
        chessBoard.move(requestDto.getFrom(), requestDto.getTo());
        this.turn = Player.reversePlayer(turn);
        return new ResponseDto(chessBoard, chessBoard.createResult());
    }

    public ResponseDto status(RequestDto requestDto){
        return new ResponseDto(chessBoard, chessBoard.createResult());
    }

    public ResponseDto end(RequestDto requestDto) {
        return new ResponseDto(chessBoard, chessBoard.createResult());
    }
}
