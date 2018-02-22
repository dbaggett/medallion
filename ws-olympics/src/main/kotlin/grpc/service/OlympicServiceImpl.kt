package grpc.service

import common.services.grpc.olympicgame.*
import domain.model.Season
import io.grpc.stub.StreamObserver

class OlympicServiceImpl : OlympicGameServiceGrpc.OlympicGameServiceImplBase() {

    private val store = setOf(
            domain.model.OlympicGame(1, 2018, Season.WINTER, "Pyeongchang", "South Korea")
    )

    private fun domain.model.OlympicGame.toMessage(): OlympicGame {
        return OlympicGame
                .newBuilder()
                .setYear(year)
                .setSeason(season.toString())
                .setCity(city)
                .setHost(country)
                .build()
    }

    override fun getAllOlympicGames(request: Empty?, responseObserver: StreamObserver<OlympicGames>?) {
        val responseBuilder = OlympicGames.newBuilder().addAllOlympics(store.map {it.toMessage()})

        responseObserver?.onNext(responseBuilder.build())
        responseObserver?.onCompleted()
    }

    override fun getOlympicGame(request: OlympicGameRequest?, responseObserver: StreamObserver<OlympicGame>?) {
        val game = store.find { it.id == request?.id }

        responseObserver?.onNext(game?.toMessage())
        responseObserver?.onCompleted()
    }
}