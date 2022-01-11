package com.project2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String EXCHANGE_TOPIC = "topic_logs";
    private static final String EXCHANGE_FANOUT = "logs";
    /**
     * Publish Messages with different routing keys.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void publishMessage(String message, String routingKey) throws IOException, TimeoutException {
//        TopicExchange.declareExchange();
        try(Channel channel = ConnectionManager.getConnection().createChannel()){
            channel.basicPublish(EXCHANGE_TOPIC, routingKey, null, message.getBytes());
            System.out.println(" [x] Sent '" + "health.education" + "':'" + message + "'");
        }
//        channel.exchangeDeclare(EXCHANGE_TOPIC,"topic");
//        channel.close();
    }
}
