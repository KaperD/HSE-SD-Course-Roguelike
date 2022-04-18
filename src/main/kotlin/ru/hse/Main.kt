package ru.hse

import com.rabbitmq.client.*
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val serverAddress = args.firstOrNull()
    val initialExchangeName = args.getOrNull(1)
    val clientName = args.getOrNull(2)

    if (serverAddress == null || initialExchangeName == null || clientName == null) {
        println("Usage RabbinMQ <server address> <initial exchange name> <your name>")
        exitProcess(1)
    }

    MessageClient(serverAddress, initialExchangeName, clientName).use {
        while (true) {
            val message = readLine()
            if (message == null || message == "!exit") {
                return
            }
            if (message.startsWith("!switch")) {
                val newExchangeName = message.drop("!switch".length).trim()
                it.switchExchange(newExchangeName)
            } else if (message.isNotBlank()) {
                it.sendMessage(message)
            }
        }
    }
}

class MessageClient(
    serverAddress: String,
    private var currentExchangeName: String,
    private val clientName: String
) : AutoCloseable {
    private val connection: Connection
    private val channel: Channel
    private var currentQueueName = ""
    private var currentConsumerTag = ""

    init {
        val factory = ConnectionFactory()
        factory.host = serverAddress
        connection = factory.newConnection()
        channel = connection.createChannel()
        switchExchange(currentExchangeName, false)
    }

    fun sendMessage(message: String) {
        channel.basicPublish(currentExchangeName, "", null, "$currentQueueName $clientName: $message".toByteArray())
    }

    fun switchExchange(newExchangeName: String, needCancel: Boolean = true) {
        currentExchangeName = newExchangeName
        if (needCancel) {
            channel.basicCancel(currentConsumerTag)
        }
        channel.exchangeDeclare(currentExchangeName, BuiltinExchangeType.FANOUT, true)
        currentQueueName = channel.queueDeclare().queue
        channel.queueBind(currentQueueName, currentExchangeName, "")
        val deliverCallback = DeliverCallback { _, delivery ->
            val (queue, message) = String(delivery.body, StandardCharsets.UTF_8).split(" ", limit = 2)
            if (queue != currentQueueName) {
                println(message)
            }
        }
        currentConsumerTag = channel.basicConsume(currentQueueName, true, deliverCallback) { _ -> }
    }

    override fun close() {
        channel.close()
        connection.close()
    }
}
