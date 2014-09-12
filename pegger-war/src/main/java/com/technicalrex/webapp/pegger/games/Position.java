package com.technicalrex.webapp.pegger.games;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {
    private final int row;
    private final int column;

    @JsonCreator
    public Position(@JsonProperty("row") int row,
                    @JsonProperty("column") int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isAdjacentTo(Position position) {
        if (row == position.row) {
            return column == position.column - 1 || column == position.column + 1;
        } else if (column == position.column) {
            return row == position.row - 1 || row == position.row + 1;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (column != position.column) return false;
        if (row != position.row) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Position");
        sb.append("{row=").append(row);
        sb.append(", column=").append(column);
        sb.append('}');
        return sb.toString();
    }
}
