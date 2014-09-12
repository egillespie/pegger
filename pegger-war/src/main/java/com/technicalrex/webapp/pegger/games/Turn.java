package com.technicalrex.webapp.pegger.games;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

public class Turn {
    private final int pegId;
    private final Position fromPosition;
    private final Position toPosition;

    @JsonCreator
    public Turn(@JsonProperty("pegId") int pegId,
                @JsonProperty("fromPosition") Position fromPosition,
                @JsonProperty("toPosition") Position toPosition) {
        this.pegId = pegId;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Turn turn = (Turn) o;

        if (pegId != turn.pegId) return false;
        if (!fromPosition.equals(turn.fromPosition)) return false;
        if (!toPosition.equals(turn.toPosition)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pegId;
        result = 31 * result + fromPosition.hashCode();
        result = 31 * result + toPosition.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Turn");
        sb.append("{pegId=").append(pegId);
        sb.append(", fromPosition=").append(fromPosition);
        sb.append(", toPosition=").append(toPosition);
        sb.append('}');
        return sb.toString();
    }
}
