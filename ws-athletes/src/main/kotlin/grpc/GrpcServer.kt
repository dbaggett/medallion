package grpc

import com.google.inject.Singleton
import grpc.service.AthleteServiceImpl
import io.grpc.Server
import io.grpc.ServerBuilder
import java.io.IOException

@Singleton
class GrpcServer {

    private var server: Server? = null

    @Throws(IOException::class)
    fun start(port: Int) {

        server = ServerBuilder.forPort(port)
                .addService(AthleteServiceImpl())
                .build()
                .start()

        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("Shutting down gRPC server...")
                this@GrpcServer.stop()
            }
        })
    }

    fun stop() {
        server?.shutdown()
    }

    @Throws(InterruptedException::class)
    fun blockUntilShutdown() {
        server?.awaitTermination()
    }
}