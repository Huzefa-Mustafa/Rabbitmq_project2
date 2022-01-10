package com.project2;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
public class Main {
    /**
     * Execute the methods.
     *
     * @param args
     * @throws IOException
     * @throws TimeoutException
     */
    public static void main(String[] args) throws IOException, TimeoutException{
//        connectionRabbit();
        consoleInterface();
        TopicExchange.declareExchange();
        TopicExchange.declareQueues();
        TopicExchange.declareBindings();


        Thread publish = new Thread(() -> {
            try {
                Channel channel = ConnectionManager.getConnection().createChannel();
                String message = "Drink a lot of Water and stay Healthy!";
                System.out.println("At producer");
                //channel.basicPublish("my-topic-exchange", "sports.sports.sports", null, message.getBytes());
                channel.basicPublish("my-topic-exchange", "health.education", null, message.getBytes());
                System.out.println(" [x] Sent '" + "health.education" + "':'" + message + "'");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Thread subscribe = new Thread(() -> {
            try {
                Channel channel = ConnectionManager.getConnection().createChannel();
                DeliverCallback deliverCallback =(consumerTag, delivery)->{
                    System.out.println("At consumer");
                    System.out.println("\n\n=========== Health Queue ==========");
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println("HealthQ: " + new String(delivery.getBody()));
                    System.out.println(" [x] Received '" +
                            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                };
                channel.basicConsume("HealthQ", true, deliverCallback, consumerTag -> { });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        publish.start();
        subscribe.start();
    }

    private static void connectionRabbit() {
       try {
           //Creating a connection to the server
           ConnectionFactory factory = new ConnectionFactory();
           // Inserting the data of RabbitMQ administration account
           factory.setUsername("studentx");
           factory.setPassword("studentx");

           // Inserting the IP of the machine where the server is running
           factory.setHost("127.0.0.1");
           factory.setPort(5672);
           Connection connection = factory.newConnection();
           Channel channel = connection.createChannel();
       } catch (IOException | TimeoutException e) {
           System.out.println("Server is down at Moment");
       }
    }

    private static void consoleInterface() {
        menuApp();
    }

    private static void menuApp() {
        System.out.println("=========================");
        System.out.println("|        APP MENU       |");
        System.out.println("=========================");
        System.out.println("|Options:               |");
        System.out.println("|       1.Publish Blogs |");
        System.out.println("|       2.Read Blogs    |");
        System.out.println("|       0.Exit          |");
        System.out.println("=========================");

    }
}
