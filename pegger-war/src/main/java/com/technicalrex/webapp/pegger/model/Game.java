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
    private final Optional<Turn> lastTurn;
    private final ImmutableSet<Peg> pegs;
    private final boolean gameOver;

    private Game(UUID gameId, Turn lastTurn, ImmutableSet<Peg> pegs) {
        this.gameId = Preconditions.checkNotNull(gameId);
        this.lastTurn = Optional.fromNullable(lastTurn);
        this.pegs = Preconditions.checkNotNull(pegs);
        this.gameOver = calculateGameOver();
        validateGameState();
    }

    public static Game start(UUID gameId) {
        return new Game(gameId, null, START_PEGS);
    }

    public Game movePeg(Peg pegWithNewPosition) {
        Position fromPosition = null;
        ImmutableSet.Builder<Peg> pegBuilder = ImmutableSet.builder();
        for (Peg peg : pegs) {
            if (peg.getPegId() == pegWithNewPosition.getPegId()) {
                pegBuilder.add(pegWithNewPosition);
                fromPosition = peg.getPosition();
            } else {
                pegBuilder.add(peg);
            }
        }
        if (fromPosition == null) {
            throw new IllegalStateException(String.format("Peg %d does not exist.", pegWithNewPosition.getPegId()));
        }
        Turn turn = new Turn(pegWithNewPosition.getPegId(), fromPosition, pegWithNewPosition.getPosition());
        return new Game(gameId, turn, pegBuilder.build());
    }

    public UUID getGameId() {
        return gameId;
    }

    public Optional<Turn> getLastTurn() {
        return lastTurn;
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
            }
            if (position.getColumn() < 1 || position.getColumn() > COLUMNS) {
                throw new IllegalStateException(String.format("Peg %d is at an invalid column on the board.", peg.getPegId()));
            }

            int pegIdCount = 0;
            int positionCount = 0;
            for (Peg otherPeg : pegs) {
                if (peg.getPegId() == otherPeg.getPegId()) {
                    pegIdCount++;
                }
                if (peg.getPosition().equals(otherPeg.getPosition())) {
                    positionCount++;
                }
            }
            if (pegIdCount != 1) {
                throw new IllegalStateException("All peg identifiers on a board must be unique.");
            }
            if (positionCount != 1) {
                throw new IllegalStateException("All pegs on a board must be at different positions.");
            }
        }
    }

    public static class Turn {
        private final int pegId;
        private final Position fromPosition;
        private final Position toPosition;

        private Turn(int pegId, Position fromPosition, Position toPosition) {
            this.pegId = Preconditions.checkNotNull(pegId);
            this.fromPosition = Preconditions.checkNotNull(fromPosition);
            this.toPosition = Preconditions.checkNotNull(toPosition);
        }

        public int getPegId() {
            return pegId;
        }

        public Position getFromPosition() {
            return fromPosition;
        }

        public Position getToPosition() {
            return toPosition;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("pegId", pegId)
                    .add("fromPosition", fromPosition)
                    .add("toPosition", toPosition)
                    .toString();
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
        return Objects.equal(this.gameId, that.gameId)
                && Objects.equal(this.lastTurn, that.lastTurn)
                && Objects.equal(this.pegs, that.pegs)
                && Objects.equal(this.gameOver, that.gameOver);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gameId, lastTurn, pegs, gameOver);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("gameId", gameId)
                .add("lastTurn", lastTurn)
                .add("gameOver", gameOver)
                .add("pegs", pegs)
                .toString();
    }
}
