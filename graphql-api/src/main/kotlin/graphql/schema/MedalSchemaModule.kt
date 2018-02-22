package graphql.schema

import com.google.api.graphql.rejoiner.Query
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.medal.AthleteMedalsRequest
import common.services.grpc.medal.MedalServiceGrpc
import common.services.grpc.medal.Medals

class MedalSchemaModule : SchemaModule() {

    @Query("getMedals")
    fun getMedalsForAthlete(request: AthleteMedalsRequest, client: MedalServiceGrpc.MedalServiceFutureStub): ListenableFuture<Medals> {
        return client.getMedalsForAthlete(request)
    }
}