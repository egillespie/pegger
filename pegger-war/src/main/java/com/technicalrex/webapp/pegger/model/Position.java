package com.technicalrex.webapp.pegger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position that = (Position) o;
        return Objects.equal(this.row, that.row)
                && Objects.equal(this.column, that.column);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(row, column);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("row", row)
                .add("column", column)
                .toString();
    }
}
