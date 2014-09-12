package com.technicalrex.webapp.pegger.games;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import java.util.UUID;

public class Game {
    private final UUID gameId;
    private final Optional<Turn> lastTurn;
    private final Board board;

    public Game(UUID gameId, Board board) {
        this(gameId, null, board);
    }

    public Game(UUID gameId, Turn lastTurn, Board board) {
        this.gameId = gameId;
        this.lastTurn = Optional.fromNullable(lastTurn);
        this.board = Preconditions.checkNotNull(board);
    }

    public UUID getGameId() {
        return gameId;
    }

    public Optional<Turn> getLastTurn() {
        return lastTurn;
    }

    public boolean isGameOver() {
        for (Peg peg : board.getPegs()) {
            if (peg.getType().isNeutral()) {
                continue;
            }
            for (Peg testPeg : board.getPegs()) {
                if (peg.getPegId() != testPeg.getPegId() && peg.getType() == testPeg.getType()
                        && (peg.getPosition().isAdjacentTo(testPeg.getPosition()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (!board.equals(game.board)) return false;
        if (!gameId.equals(game.gameId)) return false;
        if (!lastTurn.equals(game.lastTurn)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = gameId.hashCode();
        result = 31 * result + lastTurn.hashCode();
        result = 31 * result + board.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Game");
        sb.append("{gameId=").append(gameId);
        sb.append(", lastTurn=").append(lastTurn);
        sb.append(", board=").append(board);
        sb.append('}');
        return sb.toString();
    }
}
