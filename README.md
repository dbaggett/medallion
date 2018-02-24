A contrived Olympic Games example to demonstrate the aggregation of gRPC enabled web-services into a GraphQL middleware.

Each service corresponds to a resource out of this [Olympics API](https://olympicsapi.docs.apiary.io). The
types and fields in the graph may differ from this spec as it was simply used for inspiration and reference. _The
data produced by each service does not represent actual Olympic facts._

# Features
* Microservice design
* gRPC for inter-process communication
* GraphQL api for data aggregation from each service
* [Rejoiner](http://rejoiner.io/) used to generate aggregated GraphQL schema from proto's and gRPC clients

# How to run

## Locally

### Start each gRPC service
_Each service must be running before starting the GraphQL server._

```
./gradlew ws-athletes:run
```

```
./gradlew ws-countries:run
```

```
./gradlew ws-events:run
```

```
./gradlew ws-medals:run
```

```
./gradlew ws-olympics:run
```

### Start GraphQL server
```
./gradlew graphql-api:run
```

# Querying the GraphQL server
A [GraphiQL](https://github.com/graphql/graphiql) instance is available at http://localhost:8080.

## Introspection
Run [this](https://gist.github.com/craigbeck/b90915d49fda19d5b2b17ead14dcd6da) introspection query to learn about supported types and their fields manually.

If your using an apollo client, [apollo-codegen](https://github.com/apollographql/apollo-codegen) can be used to generate the required schema file.


## Http Examples

Post to http://localhost:8080/graphql

### Olympics
```json
{"query":"query Olympics {olympics {id city host year season}}","variables":null,"operationName":"Olympics"}
```


