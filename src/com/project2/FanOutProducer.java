package com.project2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FanOutProducer {
    private static final String EXCHANGE_FANOUT = "logs";
    public FanOutProducer() {
        //Creating connection the server
        ConnectionFactory factory = new ConnectionFactory();

        //Inserting data of our RabbitMQ administration account
        factory.setUsername("studentx");
        factory.setPassword("studentx");

        //Inserting the IP of a server where machine is running
        factory.setHost("127.0.0.1");
        factory.setPort(5672);

        //Creating connection to the server
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_FANOUT, "fanout");

            String message = "info: Hello World";

            channel.basicPublish(EXCHANGE_FANOUT, "", null, message.getBytes("UTF-8"));
            System.out.println("[x] Sent '" + message + "'");

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
