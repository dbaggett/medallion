package app

import graphql.GraphQLServer

class Server {

    private var graphql = GraphQLServer()

    fun start() {
        val port = 8080

        graphql.start(port)
    }
}

fun main(args: Array<String>) {
    Server().start()
}