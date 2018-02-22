package graphql.di

import com.google.inject.AbstractModule
import common.services.grpc.medal.MedalServiceGrpc
import io.grpc.ManagedChannelBuilder

class MedalClientModule : AbstractModule() {

    override fun configure() {
        val channel = ManagedChannelBuilder.forAddress("localhost", 50002).usePlaintext(true).build()
        bind(MedalServiceGrpc.MedalServiceFutureStub::class.java).toInstance(MedalServiceGrpc.newFutureStub(channel))
        bind(MedalServiceGrpc.MedalServiceBlockingStub::class.java).toInstance(MedalServiceGrpc.newBlockingStub(channel))
    }
}