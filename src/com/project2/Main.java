package com.project2;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    private static final String EXCHANGE_TOPIC = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException{
        declareQueues();
        declareBindings();
        consoleInterface();
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
            blogs();
//            TopicExchange.declareQueues();
            String message = "Drink a lot of Water and stay Healthy!";
            System.out.println("At producer");
            String routingKey ="health.education";
            Producer.publishMessage(message, routingKey);
        } else if (choice == 2) {
            TopicExchange.declareExchange();
            String queueName = TopicExchange.declareQueues();
            Consumer.subscribeMessage(queueName);
        } else if (choice == 4) {
            new FanOutProducer();
        }

    }

    private static void blogs() throws IOException {

        System.out.println("|Select topics          |");
        System.out.println("|       1.HealthQ       |");
        System.out.println("|       2.SportsQ       |");
        System.out.println("|       3.EducationQ    |");
        String input = scanner.nextLine();
        if (checkIfDigit(input)) choice = Integer.parseInt(input);
        else choice = 10;
        if (choice == 1) {
            health();
        } else if (choice == 2) {
            sport();
        } else if (choice == 3) {
            educationQ();
        }
    }

    private static void educationQ() throws IOException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        String message = "Stay fit in Mind and Body";
        channel.basicPublish("my-topic-exchange", "education.health", null, message.getBytes("UTF-8"));
    }

    private static void sport() throws IOException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        String message = "Learn something new everyday";
        channel.basicPublish(EXCHANGE_TOPIC, "education", null, message.getBytes("UTF-8"));
    }

    private static void health() throws IOException {

        Channel channel = ConnectionManager.getConnection().createChannel();
        String message = "Drink a lot of Water and stay Healthy!";
        channel.basicPublish(EXCHANGE_TOPIC, "health.education", null, message.getBytes("UTF-8"));
    }

    public static void declareQueues() throws IOException, TimeoutException {
        //Create a channel - do not share the Channel instance
        Channel channel = ConnectionManager.getConnection().createChannel();

        //Create the Queues
        channel.queueDeclare("HealthQ", true, false, false, null);
        channel.queueDeclare("SportsQ", true, false, false, null);
        channel.queueDeclare("EducationQ", true, false, false, null);


    }
    public static void declareBindings() throws IOException, TimeoutException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        //Create bindings - (queue, exchange, routingKey) - routingKey != null
        channel.queueBind("HealthQ", EXCHANGE_TOPIC, "health.*");
        channel.queueBind("SportsQ", EXCHANGE_TOPIC, "#.sports.*");
        channel.queueBind("EducationQ", EXCHANGE_TOPIC, "#.education");
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
