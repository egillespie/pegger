package com.technicalrex.webapp.pegger.games;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.UUID;

@Service
public class GameOperator {
    private final GameRepository gameRepository;

    @Inject
    public GameOperator(GameRepository gameRepository) {
        this.gameRepository = Preconditions.checkNotNull(gameRepository);
    }

    public Game startGame() {
        Game game = new Game(gameRepository.nextId(), Board.START);
        gameRepository.save(game);
        return game;
    }

    public Iterable<Game> listAllGames() {
        return gameRepository.getAll();
    }

    public Optional<Game> lookForGame(UUID gameId) {
        return gameRepository.getById(gameId);
    }

    public Game playTurn(Game game, Turn turn) {
        if (game.isGameOver()) {
            throw new InvalidTurnException("The game is over. No additional turns may be played.");
        }
        validateTurn(game, turn);
        Game newGame = movePeg(game, turn);
        gameRepository.update(newGame);
        return newGame;
    }

    private void validateTurn(Game game, Turn turn) {
        Position toPosition = turn.getToPosition();
        if (toPosition.getColumn() < 1 || toPosition.getColumn() > game.getBoard().getColumns()) {
            throw new InvalidTurnException("Peg cannot be moved to that column.");
        }
        if (toPosition.getRow() < 1 || toPosition.getRow() > game.getBoard().getRows()) {
            throw new InvalidTurnException("Peg cannot be moved to that row.");
        }
        if (toPosition.equals(turn.getFromPosition())) {
            throw new InvalidTurnException("The peg must be moved.");
        }

        Peg movingPeg = null;
        for (Peg peg : game.getBoard().getPegs()) {
            if (turn.getToPosition().equals(peg.getPosition())) {
                throw new InvalidTurnException("Another peg is in that position.");
            }

            if (peg.getPegId() == turn.getPegId()) {
                movingPeg = peg;
            }
        }

        if (movingPeg == null) {
            throw new InvalidTurnException(String.format("Peg %d does not exist.", turn.getPegId()));
        }
        if (!movingPeg.getPosition().equals(turn.getFromPosition())) {
            throw new InvalidTurnException(String.format("Peg %d does not exist at that position.", turn.getPegId()));
        }

        if (game.getLastTurn().isPresent()) {
            Turn lastTurn = game.getLastTurn().get();
            if (lastTurn.getPegId() == turn.getPegId() && lastTurn.getFromPosition().equals(turn.getToPosition())) {
                throw new InvalidTurnException("The previous turn cannot be undone.");
            }
        }
    }

    private Game movePeg(Game game, Turn turn) {
        return new Game(game.getGameId(), turn, game.getBoard().movePeg(turn.getPegId(), turn.getToPosition()));
    }
}
