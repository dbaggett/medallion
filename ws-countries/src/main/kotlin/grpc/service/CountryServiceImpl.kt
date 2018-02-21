package grpc.service

import common.services.grpc.country.*
import io.grpc.stub.StreamObserver

class CountryServiceImpl : CountryServiceGrpc.CountryServiceImplBase() {

    private val store = setOf(
            domain.model.Country(1, "South Korea"),
            domain.model.Country(2, "Russia"),
            domain.model.Country(3, "Canada"),
            domain.model.Country(4, "Norway"),
            domain.model.Country(5, "United States"),
            domain.model.Country(6, "France")
    )

    private fun domain.model.Country.toMessage(): Country {
        return Country
                .newBuilder()
                .setId(id)
                .setName(name)
                .build()
    }

    override fun getAllCountries(request: Empty?, responseObserver: StreamObserver<Countries>?) {
        val responseBuilder = Countries.newBuilder().addAllContries(store.map {it.toMessage()})

        responseObserver?.onNext(responseBuilder.build())
        responseObserver?.onCompleted()
    }

    override fun getCountry(request: CountryRequest?, responseObserver: StreamObserver<Country>?) {
        val country = store.find { it.name == request?.name }

        responseObserver?.onNext(country?.toMessage())
        responseObserver?.onCompleted()
    }
}