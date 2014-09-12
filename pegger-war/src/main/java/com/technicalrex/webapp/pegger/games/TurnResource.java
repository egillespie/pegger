package com.technicalrex.webapp.pegger.games;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
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

@Path("/games/{gameId}/turns")
@Produces(MediaType.APPLICATION_JSON)
public class TurnResource {
    private final GameOperator gameOperator;

    @Inject
    public TurnResource(GameOperator gameOperator) {
        this.gameOperator = Preconditions.checkNotNull(gameOperator);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newTurn(@Context UriInfo uriInfo, @PathParam("gameId") UUID gameId, Turn turn) {
        Optional<Game> gameResult = gameOperator.lookForGame(gameId);
        if (!gameResult.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try {
            Game game = gameOperator.playTurn(gameResult.get(), turn);
            URI location = uriInfo.getBaseUriBuilder().path("games").path("{gameId}").build(game.getGameId());
            return Response.status(Response.Status.SEE_OTHER).location(location).build();
        } catch (InvalidTurnException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getStatus()).build();
        }
    }
}
