package com.project2;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class FanOutConsumer {
    private static final String EXCHANGE_FANOUT = "logs";
    public FanOutConsumer() throws IOException, TimeoutException {
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

            channel.exchangeDeclare(EXCHANGE_FANOUT, "fanout");

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_FANOUT, "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());

                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                DataHolder[] msg = new Gson().fromJson(message,DataHolder[].class);
                List<DataHolder> list = Arrays.asList(msg);
                for (DataHolder dataHolder : list) {
                    System.out.println(dataHolder.getQueueName());
                }
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
