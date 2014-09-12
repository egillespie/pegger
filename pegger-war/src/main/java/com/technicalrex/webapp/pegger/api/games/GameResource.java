package com.technicalrex.webapp.pegger.api.games;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.technicalrex.webapp.pegger.model.Game;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {
    private final GameOperator gameOperator;

    @Inject
    public GameResource(GameOperator gameOperator) {
        this.gameOperator = Preconditions.checkNotNull(gameOperator);
    }

    @POST
    public Response newGame(@Context UriInfo uriInfo) {
        Game game = gameOperator.startGame();
        URI location = uriInfo.getAbsolutePathBuilder().path("{arg1}").build(game.getGameId());
        return Response.created(location).entity(game).build();
    }

    @GET
    public Response getAllGames() {
        Iterable<Game> games = gameOperator.listAllGames();
        return Response.ok(games).build();
    }
    @GET
    @Path("/{gameId}")
    public Response getGame(@PathParam("gameId") UUID gameId) {
        Optional<Game> gameResult = gameOperator.lookForGame(gameId);
        if (!gameResult.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(gameResult.get()).build();
    }
}
