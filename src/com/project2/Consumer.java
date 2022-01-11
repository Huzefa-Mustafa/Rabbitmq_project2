package com.project2;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Consumer {
    private static final String EXCHANGE_NAME = "topic_logs";
    private static Channel channel;
    /**
     * Assign Consumers to each of the Queue.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void subscribeMessage(DataHolder dataHolder) throws IOException, TimeoutException {
//        TopicExchange.declareExchange();
        String queueName = TopicExchange.declareQueues(dataHolder.getRoutingKey());
        channel = ConnectionManager.getConnection().createChannel();
        final String[] dyingConsumerTag = {""};

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("\n\n=========== "+ dataHolder.getRoutingKey() +" Tags ==========");
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            System.out.println("Consumer Tag: " + consumerTag);
            dataHolder.setConsumerTag(consumerTag);
            dyingConsumerTag[0] = consumerTag;
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("Canceled Consumer tag: " + consumerTag);
        };
        channel.basicConsume(queueName, true, dyingConsumerTag[0],deliverCallback, cancelCallback);
    }

    public static void unsubscribeBlogs(DataHolder dataHolder) throws IOException, TimeoutException {
//        Channel channel =ConnectionManager.getConnection().createChannel();
        System.out.println("test");

        System.out.println(dataHolder.getConsumerTag());
        channel.basicCancel(dataHolder.getConsumerTag());



/*        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleCancel(String consumerTag) throws IOException {
                // consumer has been cancelled unexpectedly
                System.out.println(consumerTag);
            }
        };
        channel.basicConsume(dataHolder.getQueueName(), consumer);*/
    }
}
