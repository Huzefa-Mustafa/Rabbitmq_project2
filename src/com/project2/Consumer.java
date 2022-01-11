package com.project2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

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
//        String consumerTag = UUID.randomUUID().toString();
        channel.basicConsume(queueName, true, ((consumerTag, delivery) -> {
            System.out.println("\n\n=========== "+ dataHolder.getRoutingKey() +" Tags ==========");
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            System.out.println("Consumer Tag: " + consumerTag);
            dataHolder.setConsumerTag(consumerTag);
        }), consumerTag -> {
            System.out.println("Consumer Tag: " + consumerTag);
        });
    }

    public static void unsubscribeBlogs(DataHolder dataHolder) throws IOException, TimeoutException {

        System.out.println("test");



        String queueName = TopicExchange.declareQueues(dataHolder.getRoutingKey());
        Channel channel = ConnectionManager.getConnection().createChannel();
//        String consumerTag = UUID.randomUUID().toString();
        try {
            channel.basicConsume(queueName, false, ((consumerTag, delivery) -> {
                System.out.println("\n\n=========== "+ dataHolder.getRoutingKey() +" Tags ==========");

                System.out.println(" [x] Unsubscribed '" +
                        delivery.getEnvelope().getRoutingKey() + "':'"  );
                System.out.println("Consumer Tag: " + consumerTag);
                channel.basicCancel(consumerTag);
            }), consumerTag -> {
                System.out.println("Consumer Tag: " + consumerTag);
            });
        } catch (IOException e) {
            System.out.println("Error");
        }

        System.out.println(dataHolder.getConsumerTag());
        channel.basicCancel(queueName, false, ((consumerTag, delivery) -> {
        try {
            channel.basicCancel(dataHolder.getConsumerTag());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
