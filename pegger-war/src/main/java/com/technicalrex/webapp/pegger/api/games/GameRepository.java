package com.technicalrex.webapp.pegger.api.games;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.technicalrex.webapp.pegger.model.Game;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
public class GameRepository {
    private static final Map<UUID, Game> REPO = Maps.newConcurrentMap();

    public UUID nextId() {
        return UUID.randomUUID();
    }

    public void save(Game game) {
        if (REPO.containsKey(game.getGameId())) {
            throw new UnsupportedOperationException(String.format("Game with ID %s already exists.", game.getGameId()));
        }
        REPO.put(game.getGameId(), game);
    }

    public void update(Game game) {
        if (!REPO.containsKey(game.getGameId())) {
            throw new UnsupportedOperationException(String.format("Game with ID %s does not exist.", game.getGameId()));
        }
        REPO.put(game.getGameId(), game);
    }

    public Iterable<Game> getAll() {
        return REPO.values();
    }

    public Optional<Game> deleteById(UUID gameId) {
        return Optional.fromNullable(REPO.remove(gameId));
    }

    public Optional<Game> getById(UUID gameId) {
        return Optional.fromNullable(REPO.get(gameId));
    }
}
