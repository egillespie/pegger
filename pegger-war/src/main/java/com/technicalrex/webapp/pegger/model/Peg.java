package com.technicalrex.webapp.pegger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

public class Peg {
    private final int pegId;
    private final Type type;
    private final Position position;

    @JsonCreator
    public Peg(@JsonProperty("pegId") int pegId,
               @JsonProperty("type") Type type,
               @JsonProperty("position") Position position) {
        this.pegId = Preconditions.checkNotNull(pegId);
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

        @JsonCreator
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
            return MoreObjects.toStringHelper(this)
                    .add("name", name)
                    .add("neutral", neutral)
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
        Peg that = (Peg) o;
        return Objects.equal(this.pegId, that.pegId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pegId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pegId", pegId)
                .add("type", type)
                .add("position", position)
                .toString();
    }
}
