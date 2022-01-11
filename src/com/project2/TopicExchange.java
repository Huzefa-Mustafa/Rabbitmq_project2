package com.project2;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicExchange {
    private static final String EXCHANGE_NAME = "topic_logs";
    /**
     * Declare a Topic Exchange with the name my-topic-exchange.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void declareExchange() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        //Create Topic Exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
//        channel.close();
    }

    /**
     * Declare Queues to receive respective interested messages.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static String declareQueues(String bindingKey) throws IOException, TimeoutException {
        //Create a channel - do not share the Channel instance
        Channel channel = ConnectionManager.getConnection().createChannel();
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        //Create the Queues
//        channel.queueDeclare("HealthQ", true, false, false, null);
//        channel.queueDeclare("SportsQ", true, false, false, null);
//        channel.queueDeclare("EducationQ", true, false, false, null);

//        channel.close();
        return queueName;
    }
    /**
     * Declare Bindings - register interests using routing key patterns.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void declareBindings() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        //Create bindings - (queue, exchange, routingKey) - routingKey != null
//        channel.queueBind("HealthQ", "my-topic-exchange", "health.*");
//        channel.queueBind("SportsQ", "my-topic-exchange", "#.sports.*");
//        channel.queueBind("EducationQ", "my-topic-exchange", "#.education");
        channel.close();
    }

}
