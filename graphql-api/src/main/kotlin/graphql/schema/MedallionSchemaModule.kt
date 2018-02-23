package graphql.schema

import com.google.api.graphql.rejoiner.SchemaModification
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.athlete.*
import common.services.grpc.country.Country
import common.services.grpc.country.CountryRequest
import common.services.grpc.country.CountryServiceGrpc
import common.services.grpc.event.Event
import common.services.grpc.event.EventRequest
import common.services.grpc.event.EventServiceGrpc
import common.services.grpc.medal.AthleteMedalsRequest
import common.services.grpc.medal.Medal
import common.services.grpc.medal.MedalServiceGrpc
import common.services.grpc.olympicgame.OlympicGame
import io.github.vjames19.futures.jdk8.map

class MedallionSchemaModule : SchemaModule() {

    @SchemaModification(addField = "country", onType = OlympicGame::class)
    fun gameToCountry(olympicGame: OlympicGame, client: CountryServiceGrpc.CountryServiceFutureStub): ListenableFuture<Country> {
        return client.getCountry(CountryRequest.newBuilder().setName(olympicGame.host).build())
    }

    @SchemaModification(addField = "events", onType = OlympicGame::class)
    fun gameToEvents(olympicGame: OlympicGame, client: EventServiceGrpc.EventServiceFutureStub): ListenableFuture<List<Event>> {
        return client
                .getEventsByYearAndSeason(
                        EventRequest
                                .newBuilder()
                                .setSeason(olympicGame.season)
                                .setYear(olympicGame.year)
                                .build()
                ).map { it.eventsList }
    }

    @SchemaModification(addField = "olympians", onType = OlympicGame::class)
    fun getOlympicGameContestants(olympicGame: OlympicGame, client: AthleteServiceGrpc.AthleteServiceFutureStub): ListenableFuture<List<Athlete>> {
        return client
                .getAthletesByGame(
                        AthletesByGameRequest
                                .newBuilder()
                                .setSeason(olympicGame.season)
                                .setYear(olympicGame.year)
                                .build()
                ).map { it.athletesList }
    }

    @SchemaModification(addField = "olympians", onType = Event::class)
    fun getEventContestants(event: Event, client: AthleteServiceGrpc.AthleteServiceFutureStub): ListenableFuture<List<Athlete>> {
        return client
                .getAthletesByCompetition(
                        AthletesByCompetitionRequest
                                .newBuilder()
                                .setEvent(event.name)
                                .setSport(event.sport)
                                .addAllOccurrences(
                                        event.olympicsList.map {
                                            Occurrence.newBuilder().setYear(it.year).setSeason(it.season).build()
                                        }
                                )
                                .build()
                ).map { it.athletesList }
    }

    @SchemaModification(addField = "medals", onType = Athlete::class)
    fun getMedalsForAthlete(athlete: Athlete, client: MedalServiceGrpc.MedalServiceFutureStub): ListenableFuture<List<Medal>> {
        return client
                .getMedalsForAthlete(AthleteMedalsRequest.newBuilder().setAthleteId(athlete.id).build())
                .map { it.medalsList }
    }
}