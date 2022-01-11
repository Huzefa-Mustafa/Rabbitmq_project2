package com.project2;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Consumer {
    private static final String EXCHANGE_NAME = "topic_logs";
    /**
     * Assign Consumers to each of the Queue.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void subscribeMessage(DataHolder dataHolder) throws IOException, TimeoutException {
//        TopicExchange.declareExchange();
        String queueName = TopicExchange.declareQueues(dataHolder.getRoutingKey());
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.basicConsume(queueName, true, ((consumerTag, delivery) -> {
            System.out.println("\n\n=========== "+ dataHolder.getRoutingKey() +" Tags ==========");
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        }), consumerTag -> {
            System.out.println(consumerTag);
        });
    }
}
