package graphql.di

import com.google.inject.AbstractModule
import common.services.grpc.athlete.AthleteServiceGrpc
import io.grpc.ManagedChannelBuilder

class AthleteClientModule : AbstractModule() {

    override fun configure() {
        val channel = ManagedChannelBuilder.forAddress("localhost", 50005).usePlaintext(true).build()
        bind(AthleteServiceGrpc.AthleteServiceFutureStub::class.java).toInstance(AthleteServiceGrpc.newFutureStub(channel))
        bind(AthleteServiceGrpc.AthleteServiceBlockingStub::class.java).toInstance(AthleteServiceGrpc.newBlockingStub(channel))
    }
}