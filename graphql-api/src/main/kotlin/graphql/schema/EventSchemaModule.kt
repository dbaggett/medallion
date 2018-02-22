package graphql.schema

import com.google.api.graphql.rejoiner.Query
import com.google.api.graphql.rejoiner.SchemaModule
import com.google.common.util.concurrent.ListenableFuture
import common.services.grpc.event.EventRequest
import common.services.grpc.event.EventServiceGrpc
import common.services.grpc.event.Events

class EventSchemaModule : SchemaModule() {

    @Query("getEvents")
    fun getEventsByYearAndSeason(request: EventRequest, client: EventServiceGrpc.EventServiceFutureStub): ListenableFuture<Events> {
        return client.getEventsByYearAndSeason(request)
    }
}