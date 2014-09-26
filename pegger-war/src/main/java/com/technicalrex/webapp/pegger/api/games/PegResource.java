package com.technicalrex.webapp.pegger.api.games;

import com.google.common.base.Preconditions;
import com.technicalrex.webapp.pegger.api.Responses;
import com.technicalrex.webapp.pegger.model.Game;
import com.technicalrex.webapp.pegger.model.Peg;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.UUID;

@Path("/games/{gameId}/pegs")
@Produces(MediaType.APPLICATION_JSON)
public class PegResource {
    private final GameOperator gameOperator;

    @Inject
    public PegResource(GameOperator gameOperator) {
        this.gameOperator = Preconditions.checkNotNull(gameOperator);
    }

    @PUT
    @Path("/{pegId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePeg(@Context UriInfo uriInfo,
                              @PathParam("gameId") UUID gameId,
                              @PathParam("pegId") int pegId,
                              Peg peg) {
        Game game = gameOperator.lookForGame(gameId).orNull();
        if (game == null) {
            return Responses.NOT_FOUND;
        } else if (peg == null) {
            return Responses.BAD_REQUEST;
        } else if (pegId != peg.getPegId()) {
            return Responses.CONFLICT;
        }
        try {
            gameOperator.movePeg(game, peg);
            URI location = uriInfo.getBaseUriBuilder().path("games").path("{gameId}").build(game.getGameId());
            return Response.status(Response.Status.SEE_OTHER).location(location).build();
        } catch (InvalidMoveException e) {
            return Response.status(422).entity(e.getReason()).build();
        }
    }
}
