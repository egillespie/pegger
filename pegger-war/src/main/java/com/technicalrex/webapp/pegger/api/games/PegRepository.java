package com.technicalrex.webapp.pegger.api.games;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.technicalrex.webapp.pegger.model.Game;
import com.technicalrex.webapp.pegger.model.Peg;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.UUID;

@Repository
public class PegRepository {
    private final GameRepository gameRepository;

    @Inject
    public PegRepository(GameRepository gameRepository) {
        this.gameRepository = Preconditions.checkNotNull(gameRepository);
    }

    public Optional<Peg> getById(UUID gameId, int pegId) {
        Game game = gameRepository.getById(gameId).orNull();
        return game == null ? Optional.<Peg>absent() : game.getPeg(pegId);
    }
}
