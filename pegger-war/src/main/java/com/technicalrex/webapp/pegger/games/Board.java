package com.technicalrex.webapp.pegger.games;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class Board {
    public static Board START = new Board(
            new Peg(1, Peg.Type.RED, new Position(1, 1)),
            new Peg(2, Peg.Type.RED, new Position(2, 4)),
            new Peg(3, Peg.Type.GREEN, new Position(1, 4)),
            new Peg(4, Peg.Type.GREEN, new Position(2, 1)),
            new Peg(5, Peg.Type.YELLOW, new Position(1, 2)),
            new Peg(6, Peg.Type.YELLOW, new Position(2, 3)));

    private final int rows = 2;
    private final int columns = 4;
    private final ImmutableList<Peg> pegs;

    private Board(Peg... pegs) {
        this(ImmutableList.copyOf(Preconditions.checkNotNull(pegs)));
    }

    private Board(ImmutableList<Peg> pegs) {
        this.pegs = pegs;
        validatePegs();
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public ImmutableList<Peg> getPegs() {
        return pegs;
    }

    public Board movePeg(int pegId, Position toPosition) {
        ImmutableList.Builder<Peg> builder = ImmutableList.builder();
        for (Peg peg : pegs) {
            if (peg.getPegId() == pegId) {
                builder.add(new Peg(pegId, peg.getType(), toPosition));
            } else {
                builder.add(peg);
            }
        }
        return new Board(builder.build());
    }

    private void validatePegs() {
        for (Peg peg : pegs) {
            Position position = peg.getPosition();
            if (position.getRow() < 1 || position.getRow() > rows) {
                throw new IllegalStateException(String.format("Peg %d is at an invalid row on the board.", peg.getPegId()));
            }
            if (position.getColumn() < 1 || position.getColumn() > columns) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (columns != board.columns) return false;
        if (rows != board.rows) return false;
        if (!pegs.equals(board.pegs)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rows;
        result = 31 * result + columns;
        result = 31 * result + pegs.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Board");
        sb.append("{rows=").append(rows);
        sb.append(", columns=").append(columns);
        sb.append(", pegs=").append(pegs);
        sb.append('}');
        return sb.toString();
    }
}
