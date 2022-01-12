package com.project2;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Main {
     static List<DataHolder> dataHolderList = new ArrayList<>();
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
    private static final String EXCHANGE_FANOUT = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        System.out.println("Before" + dataHolderList.size());
        declareQueues();
        new FanOutProducer();
        new FanOutConsumer();
        System.out.println("After" + dataHolderList.size());
        consoleInterface();
    }

    private static void consoleInterface() throws IOException, TimeoutException {
        while (true) {
            menuApp();
            menuSelection();
        }
    }

    private static void menuApp() {
        System.out.println("=================================");
        System.out.println("|           APP MENU            |");
        System.out.println("=================================");
        System.out.println("|Options:                       |");
        System.out.println("|       1.Publish to blogs      |");
        System.out.println("|       2.Subscribe to blogs    |");
        System.out.println("|       3.Create new blog       |");
        System.out.println("|       4.Unsubscribe to blogs  |");
        System.out.println("|       0.Exit                  |");
        System.out.println("=================================");

    }

    private static void menuSelection() throws IOException, TimeoutException {
        String input = scanner.nextLine();
        if (checkIfDigit(input)) choice = Integer.parseInt(input);
        else choice = 10;
        if (choice == 1) {
            DataHolder selectedBlog = blogs();
            Producer.publishMessage(selectedBlog);
        } else if (choice == 2) {
            DataHolder selectedTag = subscribeToBlogs();
            Consumer.subscribeMessage(selectedTag);
        } else if (choice == 3) {
            createBlogs();

            new FanOutProducer();
        } else if (choice == 4) {
            DataHolder selectedTag = unsubscribeBlogs();
            Consumer.unsubscribeBlogs(selectedTag);
        }

    }

    private static void createBlogs() throws IOException, TimeoutException {
        System.out.println(" [x] Please enter a suitable name for your blog:");
        String blogName = scanner.nextLine();
        System.out.println(" [x] Please add tags with spaces related to '" + blogName + "':");
        String tag = scanner.nextLine();
        DataHolder object = new DataHolder(blogName,tag);
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.queueDeclare(object.getQueueName(), false, false, false, null);
        channel.queueBind(object.queueName, EXCHANGE_TOPIC, object.routingKey);
        dataHolderList.add(object);
        new FanOutProducer();
    }

    private static DataHolder unsubscribeBlogs() throws IOException {
        System.out.println("|Select topics          |");
        for (int i = 0; i < dataHolderList.size(); i++) {
            System.out.println("|       " + (i + 1) + "." + dataHolderList.get(i).getRoutingKey() + "       ");
        }
        String input = scanner.nextLine();
        if (checkIfDigit(input)) {
            choice = Integer.parseInt(input);
            DataHolder dataHolder = dataHolderList.get(choice - 1);
            return dataHolder;
        } else choice = 10;
//        channel = consumer.getChannel();
//        channel.basicCancel(this.consumerTag);
        return null;
    }

    private static DataHolder subscribeToBlogs(){
        System.out.println("|Select topics          |");
        for (int i = 0; i < dataHolderList.size(); i++) {
            System.out.println("|       " + (i + 1) + "." + dataHolderList.get(i).getRoutingKey() + "       ");
        }
        String input = scanner.nextLine();
        if (checkIfDigit(input)) {
            choice = Integer.parseInt(input);
            DataHolder dataHolder = dataHolderList.get(choice - 1);
            return dataHolder;
        } else choice = 10;
        return null;
    }
    private static DataHolder blogs() throws IOException, TimeoutException {

        System.out.println("|Select topics          |");
        for (int i = 0; i < dataHolderList.size(); i++) {
            System.out.println("|       " + (i + 1) + "." + dataHolderList.get(i).getQueueName() + "       ");
        }
        String input = scanner.nextLine();
        if (checkIfDigit(input)) {
            choice = Integer.parseInt(input);
            DataHolder dataHolder = dataHolderList.get(choice - 1);
            return dataHolder;
        } else choice = 10;
        return null;

//        if (choice == 1) {
////            health();
//            Producer.publishMessage("Drink a lot of Water and stay Healthy!","health.education");
//        } else if (choice == 2) {
//            Producer.publishMessage("Drink a lot of Water and stay Healthy!","health.education");
//        } else if (choice == 3) {
//            Producer.publishMessage("Stay fit in Mind and Body","education.health");
//        }
    }


    private static void test() throws IOException {
        System.out.println("test");
        Channel channel = ConnectionManager.getConnection().createChannel();

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
        TopicExchange.declareExchange();

        DataHolder dhealth = new DataHolder("HealthQ","health.*");
        DataHolder sportsQ = new DataHolder("SportsQ","#.sports.*");
        DataHolder educationQ = new DataHolder("EducationQ","#.education");
        dataHolderList.add(dhealth);
        dataHolderList.add(sportsQ);
        dataHolderList.add(educationQ);
        //Create the Queues

        for(DataHolder dataHolder : dataHolderList){
            channel.queueDeclare(dataHolder.getQueueName(), true, false, false, null);
            channel.queueBind(dataHolder.queueName, EXCHANGE_TOPIC, dataHolder.routingKey);
        }

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
