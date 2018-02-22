package graphql.schema

import com.google.api.graphql.rejoiner.Query
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.olympicgame.*

class OlympicSchemaModule : SchemaModule() {

    @Query("getOlympics")
    fun getAllOlympicGames(request: Empty, client: OlympicGameServiceGrpc.OlympicGameServiceFutureStub): ListenableFuture<OlympicGames> {
        return client.getAllOlympicGames(request)
    }

    @Query("getOlympicGame")
    fun getOlympicGame(request: OlympicGameRequest, client: OlympicGameServiceGrpc.OlympicGameServiceFutureStub): ListenableFuture<OlympicGame> {
        return client.getOlympicGame(request)
    }
}