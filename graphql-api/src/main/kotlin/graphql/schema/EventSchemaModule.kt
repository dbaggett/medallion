package graphql.schema

import com.google.api.graphql.rejoiner.Query
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.event.Empty
import common.services.grpc.event.Event
import common.services.grpc.event.EventRequest
import common.services.grpc.event.EventServiceGrpc
import io.github.vjames19.futures.jdk8.map

class EventSchemaModule : SchemaModule() {

    @Query("events")
    fun getAllEvents(client: EventServiceGrpc.EventServiceFutureStub): ListenableFuture<List<Event>> {
        return client.getAllEvents(Empty.newBuilder().build()).map { it.eventsList }
    }

    @Query("getEvents")
    fun getEventsByYearAndSeason(request: EventRequest, client: EventServiceGrpc.EventServiceFutureStub): ListenableFuture<List<Event>> {
        return client.getEventsByYearAndSeason(request).map { it.eventsList }
    }
}