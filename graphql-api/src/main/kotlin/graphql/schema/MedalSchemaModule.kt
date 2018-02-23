package graphql.schema

import com.google.api.graphql.rejoiner.Query
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.medal.Empty
import common.services.grpc.medal.AthleteMedalsRequest
import common.services.grpc.medal.Medal
import common.services.grpc.medal.MedalServiceGrpc
import io.github.vjames19.futures.jdk8.map

class MedalSchemaModule : SchemaModule() {

    @Query("medals")
    fun getAllMedals(client: MedalServiceGrpc.MedalServiceFutureStub): ListenableFuture<List<Medal>> {
        return client.getAllMedals(Empty.newBuilder().build()).map { it.medalsList }
    }

    @Query("getMedals")
    fun getMedalsForAthlete(request: AthleteMedalsRequest, client: MedalServiceGrpc.MedalServiceFutureStub): ListenableFuture<List<Medal>> {
        return client.getMedalsForAthlete(request).map { it.medalsList }
    }
}