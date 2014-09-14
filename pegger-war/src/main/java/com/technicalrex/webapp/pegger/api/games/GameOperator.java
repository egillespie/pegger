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
        validateMove(game, pegWithNewPosition);
        Game newGame = game.movePeg(pegWithNewPosition);
        gameRepository.update(newGame);
        return newGame;
    }

    private void validateMove(Game game, Peg pegWithNewPosition) {
        if (game.isGameOver()) {
            throw new InvalidMoveException("The game is over. No additional pegs may be moved.");
        }

        Peg pegWithOldPosition = pegRepository.getById(game.getGameId(), pegWithNewPosition.getPegId()).orNull();
        if (pegWithOldPosition == null) {
            throw new InvalidMoveException(String.format("Peg %d does not exist.", pegWithNewPosition.getPegId()));
        } else if (pegWithOldPosition.getType() != pegWithNewPosition.getType()) {
            throw new InvalidMoveException("Peg type cannot be changed.");
        }

        Position toPosition = pegWithNewPosition.getPosition();
        if (toPosition.getColumn() < 1 || toPosition.getColumn() > Game.COLUMNS) {
            throw new InvalidMoveException("Peg cannot be moved to that column.");
        } else if (toPosition.getRow() < 1 || toPosition.getRow() > Game.ROWS) {
            throw new InvalidMoveException("Peg cannot be moved to that row.");
        } else if (toPosition.equals(pegWithOldPosition.getPosition())) {
            throw new InvalidMoveException("The peg must be moved.");
        }

        for (Peg peg : game.getPegs()) {
            if (toPosition.equals(peg.getPosition())) {
                throw new InvalidMoveException("Another peg is in that position.");
            }
        }

        if (game.getLastPegMoved().isPresent()) {
            Peg lastPegMoved = game.getLastPegMoved().get();
            if (lastPegMoved.getPegId() == pegWithNewPosition.getPegId() && lastPegMoved.getPosition().equals(toPosition)) {
                throw new InvalidMoveException("This peg cannot be returned to its previous location.");
            }
        }
    }
}
