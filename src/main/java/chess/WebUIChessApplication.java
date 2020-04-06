package chess;

import chess.controller.ChessController;
import chess.controller.dao.ChessBoardDao;
import chess.controller.dao.GameDao;
import chess.controller.dto.RequestDto;
import chess.controller.dto.ResponseDto;
import chess.domain.game.Command;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class WebUIChessApplication {
    private static Gson gson = new Gson();
    private static final ChessController chessController = new ChessController();
<   private static final ChessBoardDao chessBoardDao = new ChessBoardDao();
    private static final GameDao gameDao = new GameDao();

    public static void main(String[] args) {
        port(8081);
        staticFiles.location("public");

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });

          get("/loadGame", (req, res) -> {
            try {
                //            String state = gameDao.findState(roomNumber);
//            if (state.equals("playing")) {
//                Map<Position, Piece> positionPieceMap = chessBoardDao.loadGamePlaying(700);
//            } else if (state.equals("end")) {
            responseDto = chessController.start(new RequestDto(Command.START));
            gameDao.saveInitGame(responseDto);
            int roomNumber = gameDao.findMaxRoomNumber();
            chessBoardDao.saveInitChessBoard(responseDto.getChessBoardDto(), roomNumber);
                res.status(200);
                return gson.toJson(responseDto);
            } catch (IllegalArgumentException | IllegalStateException e) {
                res.status(400);
                return gson.toJson(e.getMessage());
            }
        });

        get("/move", (req, res) -> {
            try {
                int roomNumber = gameDao.findMaxRoomNumber();
                RequestDto requestDto = new RequestDto(Command.MOVE, req);
                ResponseDto responseDto = chessController.move(requestDto);
                res.status(200);
                gameDao.updateGame(responseDto);
                chessBoardDao.deleteChessBoard(700);
                chessBoardDao.saveInitChessBoard(responseDto.getChessBoardDto(), roomNumber);
//                chessBoardDao.pieceMove(responseDto);
                return gson.toJson(responseDto);
            } catch (IllegalArgumentException | IllegalStateException e) {
                res.status(400);
                return gson.toJson(e.getMessage());
            }
        });

        get("/end", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            gameDao.updateEndState(700);
            return render(model, "end.html");
        });
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
