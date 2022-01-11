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
    private static final String EXCHANGE_TOPIC = "topic_logs";
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


            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                System.out.println("At consumer");
                String message = new String(delivery.getBody());

                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println("AT fanout");
        try (Channel channel = ConnectionManager.getConnection().createChannel()) {
            channel.exchangeDeclare(EXCHANGE_FANOUT, "fanout");
            channel.exchangeDeclare(EXCHANGE_TOPIC,"topic");

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_FANOUT, "info");

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) ->{
                System.out.println("AT fanout deliverCallback");
                String message = new String(delivery.getBody());
                DataHolder[] msg = new Gson().fromJson(message,DataHolder[].class);
                for (DataHolder dataHolder : msg) {
                    System.out.print(dataHolder);
                }
                List<DataHolder> list = Arrays.asList(msg);
                System.out.println(" [x] Received '" + message + "'");
                for (DataHolder dataHolder : list) {
                    System.out.print(dataHolder);
                }
            };
            channel.basicConsume(queueName, true, deliverCallback, consumeTag -> {});


        }
    }
}
