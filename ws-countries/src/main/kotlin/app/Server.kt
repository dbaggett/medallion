package app

import grpc.GrpcServer

class Server {

    private var grpc: GrpcServer? = GrpcServer()

    fun start() {
        val port = 50004

        registerShutdownHook(this)

        grpc?.start(port)

        System.out.println("Server running...")
        grpc?.blockUntilShutdown()
    }

    fun stop() {
        grpc?.stop()
    }

    private fun registerShutdownHook(server: Server) {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                server.stop()
            }
        })
    }
}

fun main(args: Array<String>) {
    Server().start()
}