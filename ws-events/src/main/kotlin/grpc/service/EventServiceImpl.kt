package grpc.service

import common.services.grpc.event.*
import domain.model.EventType
import domain.model.Season
import domain.model.Sport
import io.grpc.stub.StreamObserver

class EventServiceImpl : EventServiceGrpc.EventServiceImplBase() {

    private val store = setOf(
            domain.model.Event(1, EventType.INDIVIDUAL, Sport.FIGURE_SKATING, listOf(domain.model.OlympicGame(2018, Season.WINTER))),
            domain.model.Event(2, EventType.PAIRS, Sport.FIGURE_SKATING, listOf(domain.model.OlympicGame(2018, Season.WINTER))),
            domain.model.Event(3, EventType.HALF_PIPE, Sport.SNOWBOARD, listOf(domain.model.OlympicGame(2018, Season.WINTER))),
            domain.model.Event(4, EventType.SLALOM, Sport.ALPING_SKING, listOf(domain.model.OlympicGame(2018, Season.WINTER))),
            domain.model.Event(5, EventType.DOWNHILL, Sport.ALPING_SKING, listOf(domain.model.OlympicGame(2018, Season.WINTER)))
    )

    private fun domain.model.OlympicGame.toMessage(): OlympicGame {
        return OlympicGame
                .newBuilder()
                .setYear(year)
                .setSeason(season.toString())
                .build()
    }

    private fun domain.model.Event.toMessage(): Event {
        return Event
                .newBuilder()
                .setId(id)
                .setName(name.toString())
                .setSport(sport.toString())
                .addAllOlympics(olympics.map { it.toMessage() })
                .build()
    }

    override fun getEventsByYearAndSeason(request: EventRequest?, responseObserver: StreamObserver<Events>?) {
        val events = store.filter { it.olympics.contains(domain.model.OlympicGame(request!!.year, Season.valueOf(request.season))) }

        val responseBuilder = Events.newBuilder().addAllEvents(events.map { it.toMessage() })

        responseObserver?.onNext(responseBuilder.build())
        responseObserver?.onCompleted()
    }
}