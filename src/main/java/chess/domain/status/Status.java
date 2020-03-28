package chess.domain.status;

import chess.domain.Player;

import java.util.Objects;

public class Status {
    private Player player;
    private double score;

    public Status(Player player, double score) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(score);
        
        this.player = player;
        this.score = score;
    }

    public Player getPlayer() {
        return player;
    }

    public double getScore() {
        return score;
    }

    public boolean isSameScore(double score) {
        return this.score == score;
    }
}
