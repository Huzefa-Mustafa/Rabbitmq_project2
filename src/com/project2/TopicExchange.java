package com.project2;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import static com.project2.Main.EXCHANGE_TOPIC;

public class TopicExchange {


    public static void declareExchange() throws IOException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        //Create Topic Exchange
        channel.exchangeDeclare(EXCHANGE_TOPIC, "topic");
    }

    public static String declareQueues(String bindingKey) throws IOException {
        //Create a channel - do not share the Channel instance
        Channel channel = ConnectionManager.getConnection().createChannel();
        String queueName = channel.queueDeclare().getQueue();

        //Create bindings - (queue, exchange, routingKey) - routingKey != null
        channel.queueBind(queueName, EXCHANGE_TOPIC, bindingKey);

        return queueName;
    }
}
