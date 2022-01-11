package com.project2;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
public class Main {
    /**
     * Execute the methods.
     *
     * @param args
     * @throws IOException
     * @throws TimeoutException
     */
    static Scanner scanner = new Scanner(System.in);
    static int choice;
    public static void main(String[] args) throws IOException, TimeoutException{
//        connectionRabbit();
/*        TopicExchange.declareExchange();
        TopicExchange.declareQueues();
        TopicExchange.declareBindings();*/
        consoleInterface();

        /*producer();
        consumer();*/

        new Producer();
        new Consumer();


/*        Thread publish = new Thread(() -> {
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
        subscribe.start();*/
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

    private static void consoleInterface() throws IOException, TimeoutException {
        while (true) {
            menuApp();
            menuSelection();
        }
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

    private static void menuSelection() throws IOException, TimeoutException {
        String input = scanner.nextLine();
        if (checkIfDigit(input)) choice = Integer.parseInt(input);
        else choice = 10;
        if (choice == 1) {
            new Producer();

        } else if (choice == 2) {
            new Consumer();
        } else if (choice == 4) {
            new FanOutProducer();
        }

    }

    private static void consumer() {
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

            channel.exchangeDeclare("my-topic-exchange", BuiltinExchangeType.TOPIC,true);
            channel.queueDeclare("HealthQ", true, false, false, null);
            channel.queueBind("HealthQ", "my-topic-exchange", "health.*");
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            DeliverCallback deliverCallback =(consumerTag, delivery)->{
                System.out.println("At consumer");
                System.out.println("\n\n=========== Health Queue ==========");
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("HealthQ: " + new String(delivery.getBody()));
                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            channel.basicConsume("HealthQ", true, deliverCallback, consumerTag -> { });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void producer() throws IOException, TimeoutException {
        //Creating a connection to the server
        ConnectionFactory factory = new ConnectionFactory();
        // Inserting the data of RabbitMQ administration account
        factory.setUsername("studentx");
        factory.setPassword("studentx");

        // Inserting the IP of the machine where the server is running
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare("my-topic-exchange", BuiltinExchangeType.TOPIC,true);

            String message = "Drink a lot of Water and stay Healthy!";
            System.out.println("At producer");
            channel.basicPublish("my-topic-exchange", "health.education", null, message.getBytes());
            System.out.println(" [x] Sent '" + "health.education" + "':'" + message + "'");

        }
    }

    static boolean checkIfDigit(String input) {
        if (input.length() == 0) return false;
        for (int i = 0; i < input.length(); i++) {
            if (!(input.charAt(i) >= '0' && input.charAt(i) <= '9')) {
                return false;
            }
        }
        return true;
    }
}
