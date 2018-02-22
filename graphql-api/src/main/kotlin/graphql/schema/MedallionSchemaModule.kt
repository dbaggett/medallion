package graphql.schema

import com.google.api.graphql.rejoiner.SchemaModification
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.athlete.Athlete
import common.services.grpc.athlete.AthleteServiceGrpc
import common.services.grpc.athlete.Athletes
import common.services.grpc.athlete.AthletesByCompetitionRequest
import common.services.grpc.country.Country
import common.services.grpc.country.CountryRequest
import common.services.grpc.country.CountryServiceGrpc
import common.services.grpc.event.Event
import common.services.grpc.event.EventRequest
import common.services.grpc.event.EventServiceGrpc
import common.services.grpc.event.Events
import common.services.grpc.medal.AthleteMedalsRequest
import common.services.grpc.medal.MedalServiceGrpc
import common.services.grpc.medal.Medals
import common.services.grpc.olympicgame.OlympicGame

class MedallionSchemaModule : SchemaModule() {

    @SchemaModification(addField = "country", onType = OlympicGame::class)
    fun gameToCountry(olympicGame: OlympicGame, client: CountryServiceGrpc.CountryServiceFutureStub): ListenableFuture<Country> {
        return client.getCountry(CountryRequest.newBuilder().setName(olympicGame.host).build())
    }

    @SchemaModification(addField = "events", onType = OlympicGame::class)
    fun gameToEvents(olympicGame: OlympicGame, client: EventServiceGrpc.EventServiceFutureStub): ListenableFuture<Events> {
        return client.getEventsByYearAndSeason(EventRequest.newBuilder().setSeason(olympicGame.season).setYear(olympicGame.year).build())
    }

    @SchemaModification(addField = "contestants", onType = Event::class)
    fun getEventContestants(event: Event, olympicGame: OlympicGame, client: AthleteServiceGrpc.AthleteServiceFutureStub): ListenableFuture<Athletes> {
        return client
                .getAthletesByCompetition(
                        AthletesByCompetitionRequest
                                .newBuilder()
                                .setEvent(event.name)
                                .setSport(event.sport)
                                .setSeason(olympicGame.season)
                                .setYear(olympicGame.year)
                                .build()
                )
    }

    @SchemaModification(addField = "medals", onType = Athlete::class)
    fun getMedalsForAthlete(athlete: Athlete, client: MedalServiceGrpc.MedalServiceFutureStub): ListenableFuture<Medals> {
        return client.getMedalsForAthlete(AthleteMedalsRequest.newBuilder().setAthleteId(athlete.id).build())
    }
}