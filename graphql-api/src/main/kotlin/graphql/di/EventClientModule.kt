package graphql.di

import com.google.inject.AbstractModule
import common.services.grpc.event.EventServiceGrpc
import io.grpc.ManagedChannelBuilder

class EventClientModule : AbstractModule() {

    override fun configure() {
        val channel = ManagedChannelBuilder.forAddress("localhost", 50003).usePlaintext(true).build()
        bind(EventServiceGrpc.EventServiceFutureStub::class.java).toInstance(EventServiceGrpc.newFutureStub(channel))
        bind(EventServiceGrpc.EventServiceBlockingStub::class.java).toInstance(EventServiceGrpc.newBlockingStub(channel))
    }
}