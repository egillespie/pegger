package com.technicalrex.webapp.pegger.api.games;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.technicalrex.webapp.pegger.model.Game;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.logging.Logger;

@Service
public class GameTools {
    private static final Logger LOGGER = Logger.getLogger(GameTools.class.getSimpleName());

    private static final long DEFAULT_MAX_AGE_IN_HOURS = 24 * 7;

    private final GameRepository gameRepository;

    @Inject
    public GameTools(GameRepository gameRepository) {
        this.gameRepository = Preconditions.checkNotNull(gameRepository);
    }

    public int removeOldGames() {
        return removeOldGames(DEFAULT_MAX_AGE_IN_HOURS);
    }

    public int removeOldGames(long maxAgeInHours) {
        Instant maxAgeTimestamp = Instant.now().minus(Duration.standardHours(maxAgeInHours));
        int totalGames = 0, gamesRemoved = 0;
        Iterable<Game> allGames = gameRepository.getAll();
        for (Game game : allGames) {
            totalGames++;
            if (game.getLastChangeTimestamp().isBefore(maxAgeTimestamp)) {
                Optional<Game> removedGame = gameRepository.deleteById(game.getGameId());
                if (removedGame.isPresent()) {
                    gamesRemoved++;
                }
            }
        }
        LOGGER.info(String.format("Removed %d games that were more than %d hours old.", gamesRemoved, maxAgeInHours));
        LOGGER.info(String.format("There are now %d active games.", totalGames - gamesRemoved));
        return gamesRemoved;
    }
}
