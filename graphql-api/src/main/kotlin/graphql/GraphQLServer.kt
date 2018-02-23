package graphql

import com.google.api.graphql.execution.GuavaListenableFutureSupport
import com.google.common.collect.ImmutableMap
import com.google.common.io.CharStreams
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import graphql.execution.instrumentation.ChainedInstrumentation
import graphql.execution.instrumentation.tracing.TracingInstrumentation
import graphql.schema.MedallionSchema
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.util.resource.Resource
import java.io.IOException
import java.lang.RuntimeException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class GraphQLServer : AbstractHandler() {

    private val marshaller = GsonBuilder().serializeNulls().create()
    private inline fun <reified T> genericType() = object: TypeToken<T>() {}.type
    private val instrumentation = ChainedInstrumentation(
            listOf(
                    GuavaListenableFutureSupport.listenableFutureInstrumentation(),
                    TracingInstrumentation()
            )
    )

    private val schema = MedallionSchema.schema
    private val graph = GraphQL.newGraphQL(schema).instrumentation(instrumentation).build()

    @Throws(Exception::class)
    fun start(port: Int) {
        val server = Server(port)

        val resourceHandler = ResourceHandler()
        resourceHandler.welcomeFiles = arrayOf("index.html")
        resourceHandler.isDirectoriesListed = false
        resourceHandler.baseResource = Resource.newResource("./src/main/resources")

        val handlerList = HandlerList()
        handlerList.handlers = arrayOf(resourceHandler, this)

        server.handler = handlerList
        server.start()
        server.join()
    }

    override fun handle(target: String?, baseRequest: Request?, request: HttpServletRequest?, response: HttpServletResponse?) {

        if ("/graphql" == target) {
            baseRequest?.isHandled = true

            val json = readJson(request)
            val query = json["query"]

            if (query == null || query !is String) {
                response?.status = 400

                return
            }


            val operationName = json["operationName"].toString()

            val variables = getVariables(json["variables"])

            val executionInput = ExecutionInput
                    .newExecutionInput()
                    .query(query)
                    .operationName(operationName)
                    .variables(variables)
                    .context(Any())
                    .build()

            val executionResult = graph.execute(executionInput)
            response?.contentType = "application/json"
            response?.status = HttpServletResponse.SC_OK
            marshaller.toJson(executionResult.toSpecification(), response?.writer)
        }
    }

    private fun getVariables(variables: Any?): Map<String, Any> {
        if (variables is Map<*, *>) {
            val pairs = variables.map { it.key as String to it.value as Any }
            return hashMapOf(*pairs.toTypedArray())
        }

        return hashMapOf()
    }

    private fun readJson(request: HttpServletRequest?): Map<String, Any> {
        try {
            val json = CharStreams.toString(request!!.reader)
            return jsonToMap(json)
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }

    private fun jsonToMap(json: String?): Map<String, Any> {
        if (json.isNullOrEmpty()) {
            return ImmutableMap.of()
        }

        val type = genericType<Map<String, Any>>()
        return Optional.ofNullable(marshaller.fromJson<Map<String, Any>>(json, type)).orElse(ImmutableMap.of())
    }
}