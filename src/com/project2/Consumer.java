package com.project2;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private static Channel channel;

    public static void subscribeMessage(DataHolder dataHolder) throws IOException {

        String queueName = TopicExchange.declareQueues(dataHolder.getRoutingKey());
        channel = ConnectionManager.getConnection().createChannel();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("\n\n=========== "+ dataHolder.getRoutingKey() +" Tags ==========");
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            System.out.println("Consumer Tag: " + consumerTag);
            dataHolder.setConsumerTag(consumerTag);
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("Canceled Consumer tag: " + consumerTag);
        };
        channel.basicConsume(queueName, true,deliverCallback, cancelCallback);
    }

    public static void unsubscribeBlogs(DataHolder dataHolder) throws IOException {

        System.out.println(dataHolder.getConsumerTag());
        channel.basicCancel(dataHolder.getConsumerTag());

    }
}
