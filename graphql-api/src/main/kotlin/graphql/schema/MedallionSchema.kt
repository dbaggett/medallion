package graphql.schema

import com.google.api.graphql.rejoiner.Schema
import com.google.api.graphql.rejoiner.SchemaProviderModule
import com.google.inject.Guice
import com.google.inject.Key
import graphql.di.*

object MedallionSchema {

    val schema: GraphQLSchema = Guice
            .createInjector(
                    SchemaProviderModule(),
                    AthleteClientModule(),
                    AthleteSchemaModule(),
                    CountryClientModule(),
                    CountrySchemaModule(),
                    EventClientModule(),
                    EventSchemaModule(),
                    MedalClientModule(),
                    MedalSchemaModule(),
                    OlympicClientModule(),
                    OlympicSchemaModule(),
                    MedallionSchemaModule()
            )
            .getInstance(Key.get(GraphQLSchema::class.java, Schema::class.java))
}