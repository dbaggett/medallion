package grpc.service

import common.services.grpc.medal.*
import domain.model.EventType
import domain.model.Rank
import domain.model.Sport
import io.grpc.stub.StreamObserver

class MedalServiceImpl : MedalServiceGrpc.MedalServiceImplBase() {

    private val store = setOf(
            domain.model.Medal(1, Rank.GOLD, 1, EventType.SLALOM, Sport.ALPINE_SKIING, 2018, "Canada"),
            domain.model.Medal(2, Rank.SILVER, 2, EventType.PAIRS, Sport.FIGURE_SKATING, 2018, "Russia"),
            domain.model.Medal(3, Rank.BRONZE, 3, EventType.DOWNHILL, Sport.ALPINE_SKIING, 2018, "Norway"),
            domain.model.Medal(4, Rank.BRONZE, 4, EventType.HALF_PIPE, Sport.SNOWBOARD, 2018, "United States"),
            domain.model.Medal(5, Rank.SILVER, 5, EventType.INDIVIDUAL, Sport.FIGURE_SKATING, 2018, "France")
    )

    private fun domain.model.Medal.toMessage(): Medal {
        return Medal
                .newBuilder()
                .setRank(rank.toString())
                .setEvent(event.toString())
                .setSport(sport.toString())
                .setYear(year)
                .setCountry(country)
                .build()
    }

    override fun getAllMedals(request: Empty?, responseObserver: StreamObserver<Medals>?) {
        val responseBuilder = Medals.newBuilder().addAllMedals(store.map { it.toMessage() })

        responseObserver?.onNext(responseBuilder.build())
        responseObserver?.onCompleted()
    }

    override fun getMedalsForAthlete(request: AthleteMedalsRequest?, responseObserver: StreamObserver<Medals>?) {
        val medals = store.filter { it.athleteId == request?.athleteId }

        val responseBuilder = Medals.newBuilder().addAllMedals(medals.map { it.toMessage() })

        responseObserver?.onNext(responseBuilder.build())
        responseObserver?.onCompleted()
    }
}