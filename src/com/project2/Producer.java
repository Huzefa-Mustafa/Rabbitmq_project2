package com.project2;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    /**
     * Publish Messages with different routing keys.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void publishMessage(String message, String routingKey) throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.basicPublish("my-topic-exchange", routingKey, null, message.getBytes());
        channel.close();
    }
}
