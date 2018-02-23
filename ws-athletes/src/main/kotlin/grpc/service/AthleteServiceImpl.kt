package grpc.service

import common.services.grpc.athlete.*
import domain.model.Competition
import domain.model.Gender
import domain.model.EventType
import domain.model.Sport
import domain.model.Season
import io.grpc.stub.StreamObserver

class AthleteServiceImpl : AthleteServiceGrpc.AthleteServiceImplBase() {

    private val store = setOf(
            domain.model.Athlete(
                    1,
                    "Tony",
                    "Sakic",
                    Gender.MALE,
                    listOf(Competition(EventType.SLALOM, Sport.ALPING_SKING, Season.WINTER, 2018, "Canada"))
            ),
            domain.model.Athlete(
                    2,
                    "Natlia",
                    "Federov",
                    Gender.FEMALE,
                    listOf(Competition(EventType.PAIRS, Sport.FIGURE_SKATING, Season.WINTER, 2018, "Russia"))
            ),
            domain.model.Athlete(
                    3,
                    "Aksel",
                    "Jansrud",
                    Gender.MALE,
                    listOf(Competition(EventType.DOWNHILL, Sport.ALPING_SKING, Season.WINTER, 2018, "Norway"))
            ),
            domain.model.Athlete(
                    4,
                    "White",
                    "Shaun",
                    Gender.MALE,
                    listOf(Competition(EventType.HALF_PIPE, Sport.SNOWBOARD, Season.WINTER, 2018, "United States"))
            ),
            domain.model.Athlete(
                    5,
                    "Cizeron",
                    "Kate",
                    Gender.MALE,
                    listOf(Competition(EventType.INDIVIDUAL, Sport.FIGURE_SKATING, Season.WINTER, 2018, "France"))
            )
    )

    private fun domain.model.Athlete.toMessage(): Athlete {
        return Athlete
                .newBuilder()
                .setId(id)
                .setFirst(first)
                .setLast(last)
                .setGender(gender.toString())
                .build()
    }

    override fun getAllAthletes(request: Empty?, responseObserver: StreamObserver<Athletes>?) {
        val responseBuilder = Athletes.newBuilder().addAllAthletes(store.map { it.toMessage() })

        responseObserver?.onNext(responseBuilder.build())
        responseObserver?.onCompleted()
    }

    override fun getAthletesByCountry(request: AthletesByCountryRequest?, responseObserver: StreamObserver<Athletes>?) {
        val athletes = store.filter { it.competitions.filter { it.country == request?.country }.isNotEmpty() }

        val responseBuilder = Athletes.newBuilder().addAllAthletes(athletes.map { it.toMessage() })

        responseObserver?.onNext(responseBuilder.build())
        responseObserver?.onCompleted()
    }

    override fun getAthletesByCompetition(request: AthletesByCompetitionRequest?, responseObserver: StreamObserver<Athletes>?) {
        val athletes = store.filter {
            it.competitions.filter {
                it.eventType == EventType.valueOf(request!!.event)
                        && it.sport == Sport.valueOf(request.sport)
                        && request.occurrencesList.map { it.year to it.season }.contains(it.year to it.season.toString())
            }.isNotEmpty()
        }

        val responseBuilder = Athletes.newBuilder().addAllAthletes(athletes.map { it.toMessage() })

        responseObserver?.onNext(responseBuilder.build())
        responseObserver?.onCompleted()
    }

    override fun getAthletesByGame(request: AthletesByGameRequest?, responseObserver: StreamObserver<Athletes>?) {
        val athletes = store.filter {
            it.competitions.filter { it.year == request!!.year && it.season.toString() == request.season }.isNotEmpty()
        }

        val responseBuilder = Athletes.newBuilder().addAllAthletes(athletes.map { it.toMessage() })

        responseObserver?.onNext(responseBuilder.build())
        responseObserver?.onCompleted()
    }
}