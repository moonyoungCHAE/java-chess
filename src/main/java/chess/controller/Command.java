package chess.controller;

import chess.Exceptions.IllegalCommandException;

import java.util.Arrays;

public enum Command {
    START("start"),
    END("end"),
    MOVE("move"),
    STATUS("status");

    private final String command;

    Command(String command) {
        this.command = command;
    }

    public static Command of(String input) {
        return Arrays.stream(values())
                .filter(command -> command.command.equals(input))
                .findFirst()
                .orElseThrow(IllegalCommandException::new);
    }
}
