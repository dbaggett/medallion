package graphql.schema

import com.google.api.graphql.rejoiner.Query
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.olympicgame.*
import io.github.vjames19.futures.jdk8.map

class OlympicSchemaModule : SchemaModule() {

    @Query("olympics")
    fun getAllOlympicGames(client: OlympicGameServiceGrpc.OlympicGameServiceFutureStub): ListenableFuture<List<OlympicGame>> {
        return client.getAllOlympicGames(Empty.newBuilder().build()).map { it.olympicsList }
    }

    @Query("getOlympicGame")
    fun getOlympicGame(request: OlympicGameRequest, client: OlympicGameServiceGrpc.OlympicGameServiceFutureStub): ListenableFuture<OlympicGame> {
        return client.getOlympicGame(request)
    }
}