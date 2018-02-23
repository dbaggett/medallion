package graphql.schema

import com.google.api.graphql.rejoiner.Query
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.country.*
import io.github.vjames19.futures.jdk8.map

class CountrySchemaModule : SchemaModule() {

    @Query("countries")
    fun getAllCountries(client: CountryServiceGrpc.CountryServiceFutureStub): ListenableFuture<List<Country>> {
        return client.getAllCountries(Empty.newBuilder().build()).map { it.contriesList }
    }

    @Query("getCountry")
    fun getCountry(request: CountryRequest, client: CountryServiceGrpc.CountryServiceFutureStub): ListenableFuture<Country> {
        return client.getCountry(request)
    }
}