package com.project2;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    /**
     * Assign Consumers to each of the Queue.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void subscribeMessage(String queueName) throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.basicConsume(queueName, true, ((consumerTag, message) -> {
            System.out.println("\n\n=========== "+ queueName +" Queue ==========");
            System.out.println(consumerTag);
            System.out.println(queueName+": " + new String(message.getBody()));
            System.out.println(message.getEnvelope());
        }), consumerTag -> {
            System.out.println(consumerTag);
        });
    }
}
