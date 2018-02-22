package graphql.di

import com.google.inject.AbstractModule
import common.services.grpc.country.CountryServiceGrpc
import io.grpc.ManagedChannelBuilder

class CountryClientModule : AbstractModule() {

    override fun configure() {
        val channel = ManagedChannelBuilder.forAddress("localhost", 50004).usePlaintext(true).build()
        bind(CountryServiceGrpc.CountryServiceFutureStub::class.java).toInstance(CountryServiceGrpc.newFutureStub(channel))
        bind(CountryServiceGrpc.CountryServiceBlockingStub::class.java).toInstance(CountryServiceGrpc.newBlockingStub(channel))
    }
}