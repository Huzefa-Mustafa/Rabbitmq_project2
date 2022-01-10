package com.project2;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
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
        consoleInterface();
        connectionRabbit();
        TopicExchange.declareExchange();
        TopicExchange.declareQueues();
        TopicExchange.declareBindings();

        Thread subscribe = new Thread(() -> {
            try {
                TopicExchange.subscribeMessage();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        });

        Thread publish = new Thread(() -> {
            try {
                TopicExchange.publishMessage();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        });
        subscribe.start();
        publish.start();
    }

    private static void connectionRabbit() throws IOException, TimeoutException {
        //Creating a connection to the server
        ConnectionFactory factory = new ConnectionFactory();
        // Inserting the data of RabbitMQ administration account
        factory.setUsername("studentx");
        factory.setPassword("studentx");

        // Inserting the IP of the machine where the server is running
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        Connection connection = factory.newConnection();// connection interface used to open channel
        Channel channel = connection.createChannel(); // the channel can now be used to send and receive message
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
