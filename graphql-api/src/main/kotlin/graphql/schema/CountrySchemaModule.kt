package graphql.schema

import com.google.api.graphql.rejoiner.Query
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.country.*

class CountrySchemaModule : SchemaModule() {

    @Query("getCountries")
    fun getAllCountries(request: Empty, client: CountryServiceGrpc.CountryServiceFutureStub): ListenableFuture<Countries> {
        return client.getAllCountries(request)
    }

    @Query("getCountry")
    fun getCountry(request: CountryRequest, client: CountryServiceGrpc.CountryServiceFutureStub): ListenableFuture<Country> {
        return client.getCountry(request)
    }
}