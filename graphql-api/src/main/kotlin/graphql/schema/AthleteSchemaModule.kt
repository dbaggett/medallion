package graphql.schema

import com.google.api.graphql.rejoiner.Query
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.athlete.*
import io.github.vjames19.futures.jdk8.map

class AthleteSchemaModule : SchemaModule() {

    @Query("athletes")
    fun getAllAthletes(client: AthleteServiceGrpc.AthleteServiceFutureStub): ListenableFuture<List<Athlete>> {
        return client.getAllAthletes(Empty.newBuilder().build()).map { it.athletesList }
    }

    @Query("getAthletesByCountry")
    fun getAthletesByCountry(request: AthletesByCountryRequest, client: AthleteServiceGrpc.AthleteServiceFutureStub): ListenableFuture<List<Athlete>> {
        return client.getAthletesByCountry(request).map { it.athletesList }
    }

    @Query("getAthletesByCompetition")
    fun getAthletesByCompetition(request: AthletesByCompetitionRequest, client: AthleteServiceGrpc.AthleteServiceFutureStub): ListenableFuture<List<Athlete>> {
        return client.getAthletesByCompetition(request).map { it.athletesList }
    }
}