package com.project2;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static final String EXCHANGE_NAME = "topic_logs";
    public Consumer() throws IOException, TimeoutException {
        try {
            //Creating connection the server
            ConnectionFactory factory = new ConnectionFactory();

            //Inserting data of our RabbitMQ administration account
            factory.setUsername("studentx");
            factory.setPassword("studentx");

            //Inserting the IP of a server where machine is running
            factory.setHost("127.0.0.1");
            factory.setPort(5672);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String queueName = channel.queueDeclare().getQueue();

            channel.queueBind(queueName, EXCHANGE_NAME, "health.*");

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                System.out.println("At consumer");
                System.out.println("\n\n=========== Health Queue ==========");
                String message = new String(delivery.getBody(), "UTF-8");
//                System.out.println("HealthQ: " + new String(delivery.getBody()));
                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
