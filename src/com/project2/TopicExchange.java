package com.project2;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

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

    /**
     * Assign Consumers to each of the Queue.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void subscribeMessage() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        DeliverCallback deliverCallback =(consumerTag,delivery)->{
            System.out.println("\n\n=========== Health Queue ==========");
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("HealthQ: " + new String(delivery.getBody()));
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume("HealthQ", true, deliverCallback, consumerTag -> { });

        channel.basicConsume("HealthQ", true, ((consumerTag, message) -> {
            System.out.println("\n\n=========== Health Queue ==========");
            System.out.println(consumerTag);
            System.out.println("HealthQ: " + new String(message.getBody()));
            System.out.println(message.getEnvelope());
        }), consumerTag -> {
            System.out.println(consumerTag);
        });

        channel.basicConsume("SportsQ", true, ((consumerTag, message) -> {
            System.out.println("\n\n ============ Sports Queue ==========");
            System.out.println(consumerTag);
            System.out.println("SportsQ: " + new String(message.getBody()));
            System.out.println(message.getEnvelope());
        }), consumerTag -> {
            System.out.println(consumerTag);
        });

        channel.basicConsume("EducationQ", true, ((consumerTag, message) -> {
            System.out.println("\n\n ============ Education Queue ==========");
            System.out.println(consumerTag);
            System.out.println("EducationQ: " + new String(message.getBody()));
            System.out.println(message.getEnvelope());
        }), consumerTag -> {
            System.out.println(consumerTag);
        });
    }
    /**
     * Publish Messages with different routing keys.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void publishMessage() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        String message = "Drink a lot of Water and stay Healthy!";
        //channel.basicPublish("my-topic-exchange", "sports.sports.sports", null, message.getBytes());
        channel.basicPublish("my-topic-exchange", "health.education", null, message.getBytes());

        message = "Learn something new everyday";
        channel.basicPublish("my-topic-exchange", "education", null, message.getBytes());

        message = "Stay fit in Mind and Body";
        channel.basicPublish("my-topic-exchange", "education.health", null, message.getBytes());

        channel.close();
    }
}
