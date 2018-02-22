package graphql.di

import com.google.inject.AbstractModule
import common.services.grpc.olympicgame.OlympicGameServiceGrpc
import io.grpc.ManagedChannelBuilder

class OlympicClientModule : AbstractModule() {

    override fun configure() {
        val channel = ManagedChannelBuilder.forAddress("localhost", 50001).usePlaintext(true).build()
        bind(OlympicGameServiceGrpc.OlympicGameServiceFutureStub::class.java).toInstance(OlympicGameServiceGrpc.newFutureStub(channel))
        bind(OlympicGameServiceGrpc.OlympicGameServiceBlockingStub::class.java).toInstance(OlympicGameServiceGrpc.newBlockingStub(channel))
    }
}