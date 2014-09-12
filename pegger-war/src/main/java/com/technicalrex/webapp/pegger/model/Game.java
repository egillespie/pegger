package com.technicalrex.webapp.pegger.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import java.util.UUID;

public class Game {
    public static final int ROWS = 2;
    public static final int COLUMNS = 4;

    private static final ImmutableSet<Peg> START_PEGS = ImmutableSet.<Peg>builder()
            .add(new Peg(1, Peg.Type.RED, new Position(1, 1)))
            .add(new Peg(2, Peg.Type.RED, new Position(2, 4)))
            .add(new Peg(3, Peg.Type.GREEN, new Position(1, 4)))
            .add(new Peg(4, Peg.Type.GREEN, new Position(2, 1)))
            .add(new Peg(5, Peg.Type.YELLOW, new Position(1, 2)))
            .add(new Peg(6, Peg.Type.YELLOW, new Position(2, 3)))
            .build();

    private final UUID gameId;
    private final Optional<Peg> lastPegMoved;
    private final ImmutableSet<Peg> pegs;
    private final boolean gameOver;

    private Game(UUID gameId, Peg lastPegMoved, ImmutableSet<Peg> pegs) {
        this.gameId = Preconditions.checkNotNull(gameId);
        this.lastPegMoved = Optional.fromNullable(lastPegMoved);
        this.pegs = Preconditions.checkNotNull(pegs);
        this.gameOver = calculateGameOver();
        validateGameState();
    }

    public static Game start(UUID gameId) {
        return new Game(gameId, null, START_PEGS);
    }

    public Game movePeg(Peg pegWithNewPosition) {
        Peg pegWithOldPosition = null;
        ImmutableSet.Builder<Peg> pegBuilder = ImmutableSet.builder();
        for (Peg peg : pegs) {
            if (peg.getPegId() == pegWithNewPosition.getPegId()) {
                pegBuilder.add(pegWithNewPosition);
                pegWithOldPosition = peg;
            } else {
                pegBuilder.add(peg);
            }
        }
        if (pegWithOldPosition == null) {
            throw new IllegalStateException(String.format("Peg %d does not exist.", pegWithNewPosition.getPegId()));
        }
        return new Game(gameId, pegWithOldPosition, pegBuilder.build());
    }

    public UUID getGameId() {
        return gameId;
    }

    public Optional<Peg> getLastPegMoved() {
        return lastPegMoved;
    }

    public ImmutableSet<Peg> getPegs() {
        return pegs;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private boolean calculateGameOver() {
        for (Peg peg : pegs) {
            if (peg.getType().isNeutral()) {
                continue;
            }
            for (Peg testPeg : pegs) {
                if (peg.getPegId() != testPeg.getPegId() && peg.getType() == testPeg.getType()
                        && (peg.getPosition().isAdjacentTo(testPeg.getPosition()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void validateGameState() {
        Preconditions.checkArgument(pegs.size() == 6, "Exactly six pegs are required.");
        for (Peg peg : pegs) {
            Position position = peg.getPosition();
            if (position.getRow() < 1 || position.getRow() > ROWS) {
                throw new IllegalStateException(String.format("Peg %d is at an invalid row on the board.", peg.getPegId()));
            } else if (position.getColumn() < 1 || position.getColumn() > COLUMNS) {
                throw new IllegalStateException(String.format("Peg %d is at an invalid column on the board.", peg.getPegId()));
            }

            int positionCount = 0;
            for (Peg otherPeg : pegs) {
                if (peg.getPosition().equals(otherPeg.getPosition())) {
                    positionCount++;
                }
            }
            if (positionCount != 1) {
                throw new IllegalStateException("All pegs on a board must be at different positions.");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game that = (Game) o;
        return Objects.equal(this.gameId, that.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gameId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("gameId", gameId)
                .add("lastPegMoved", lastPegMoved)
                .add("gameOver", gameOver)
                .add("pegs", pegs)
                .toString();
    }
}
