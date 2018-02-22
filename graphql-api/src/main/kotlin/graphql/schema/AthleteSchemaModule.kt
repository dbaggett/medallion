package graphql.schema

import com.google.api.graphql.rejoiner.Query
import com.google.api.graphql.rejoiner.RelayNode
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.athlete.*

class AthleteSchemaModule : SchemaModule() {

    @Query("getAthletesByCountry")
    fun getAthletesByCountry(request: AthletesByCountryRequest, client: AthleteServiceGrpc.AthleteServiceFutureStub): ListenableFuture<Athletes> {
        return client.getAthletesByCountry(request)
    }

    @Query("getAthletesByCompetition")
    fun getAthletesByCompetition(request: AthletesByCompetitionRequest, client: AthleteServiceGrpc.AthleteServiceFutureStub): ListenableFuture<Athletes> {
        return client.getAthletesByCompetition(request)
    }
}