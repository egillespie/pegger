package com.technicalrex.webapp.pegger.api.games;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.technicalrex.webapp.pegger.model.Game;
import com.technicalrex.webapp.pegger.model.Peg;
import com.technicalrex.webapp.pegger.model.Position;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.UUID;

@Service
public class GameOperator {
    private final GameRepository gameRepository;
    private final PegRepository pegRepository;

    @Inject
    public GameOperator(GameRepository gameRepository, PegRepository pegRepository) {
        this.gameRepository = Preconditions.checkNotNull(gameRepository);
        this.pegRepository = Preconditions.checkNotNull(pegRepository);
    }

    public Game startGame() {
        Game game = Game.start(gameRepository.nextId());
        gameRepository.save(game);
        return game;
    }

    public Iterable<Game> listAllGames() {
        return gameRepository.getAll();
    }

    public Optional<Game> lookForGame(UUID gameId) {
        return gameRepository.getById(gameId);
    }

    public Game movePeg(Game game, Peg pegWithNewPosition) {
        if (game.isGameOver()) {
            throw new InvalidTurnException("The game is over. No additional turns may be played.");
        }
        validateMove(game, pegWithNewPosition);
        Game newGame = game.movePeg(pegWithNewPosition);
        gameRepository.update(newGame);
        return newGame;
    }

    private void validateMove(Game game, Peg pegWithNewPosition) {
        Peg pegWithOldPosition = pegRepository.getById(game.getGameId(), pegWithNewPosition.getPegId()).orNull();
        if (pegWithOldPosition == null) {
            throw new InvalidTurnException(String.format("Peg %d does not exist.", pegWithNewPosition.getPegId()));
        } else if (pegWithOldPosition.getType() != pegWithNewPosition.getType()) {
            throw new InvalidTurnException("Peg type cannot be changed.");
        }

        Position toPosition = pegWithNewPosition.getPosition();
        if (toPosition.getColumn() < 1 || toPosition.getColumn() > Game.COLUMNS) {
            throw new InvalidTurnException("Peg cannot be moved to that column.");
        } else if (toPosition.getRow() < 1 || toPosition.getRow() > Game.ROWS) {
            throw new InvalidTurnException("Peg cannot be moved to that row.");
        } else if (toPosition.equals(pegWithOldPosition.getPosition())) {
            throw new InvalidTurnException("The peg must be moved.");
        }

        for (Peg peg : game.getPegs()) {
            if (toPosition.equals(peg.getPosition())) {
                throw new InvalidTurnException("Another peg is in that position.");
            }
        }

        if (game.getLastPegMoved().isPresent()) {
            Peg lastPegMoved = game.getLastPegMoved().get();
            if (lastPegMoved.getPegId() == pegWithNewPosition.getPegId() && lastPegMoved.getPosition().equals(toPosition)) {
                throw new InvalidTurnException("The previous turn cannot be undone.");
            }
        }
    }
}
