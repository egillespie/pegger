package com.technicalrex.webapp.pegger.games;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

public class Peg {
    private final int pegId;
    private final Type type;
    private final Position position;

    public Peg(int pegId, Type type, Position position) {
        this.pegId = pegId;
        this.type = Preconditions.checkNotNull(type);
        this.position = Preconditions.checkNotNull(position);
    }

    public int getPegId() {
        return pegId;
    }

    public Type getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public static enum Type {
        RED("red", false),
        GREEN("green", false),
        YELLOW("yellow", true);

        private static final ImmutableMap<String, Type> FOR_NAME;

        static {
            final ImmutableMap.Builder<String, Type> FOR_NAME_BUILDER = ImmutableMap.builder();
            for (Type type : Type.values()) {
                FOR_NAME_BUILDER.put(type.getName(), type);
            }
            FOR_NAME = FOR_NAME_BUILDER.build();
        }

        public static Type forName(String name) {
            if (FOR_NAME.containsKey(name)) {
                return FOR_NAME.get(name);
            }
            throw new IllegalArgumentException(String.format("Invalid peg name: %s", name));
        }

        private final String name;
        private final boolean neutral;

        Type(String name, boolean neutral) {
            this.name = name;
            this.neutral = neutral;
        }

        @JsonValue
        public String getName() {
            return name;
        }

        public boolean isNeutral() {
            return neutral;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Type");
            sb.append("{name='").append(name).append('\'');
            sb.append(", neutral=").append(neutral);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Peg peg = (Peg) o;

        if (pegId != peg.pegId) return false;
        if (!position.equals(peg.position)) return false;
        if (type != peg.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pegId;
        result = 31 * result + type.hashCode();
        result = 31 * result + position.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Peg");
        sb.append("{pegId=").append(pegId);
        sb.append(", type=").append(type);
        sb.append(", position=").append(position);
        sb.append('}');
        return sb.toString();
    }
}
