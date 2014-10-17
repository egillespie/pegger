package com.technicalrex.webapp.pegger.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.technicalrex.webapp.pegger.api.games.GameTools;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tools")
@Produces(MediaType.APPLICATION_JSON)
public class ToolResource {
    private final GameTools gameTools;

    @Inject
    public ToolResource(GameTools gameTools) {
        this.gameTools = Preconditions.checkNotNull(gameTools);
    }

    @GET
    @Path("clean_games")
    public Response cleanOldGames(@QueryParam("maxAgeInHours") Long maxAgeInHours) {
        int gamesRemoved;
        if (maxAgeInHours == null) {
            gamesRemoved = gameTools.removeOldGames();
        } else {
            gamesRemoved = gameTools.removeOldGames(maxAgeInHours);
        }
        return Response.ok(ImmutableMap.of("removed", gamesRemoved)).build();
    }
}
