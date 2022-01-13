package com.project2;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Main {
    static List<DataHolder> dataHolderList = new ArrayList<>();
    static List<DataHolder> tring = new ArrayList<>();

    static Scanner scanner = new Scanner(System.in);
    static int choice, menuChoice;
    static final String EXCHANGE_TOPIC = "topic_logs";
    static final String EXCHANGE_FANOUT = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        System.out.println("Before" + dataHolderList.size());
        declareQueues();

        new FanOutConsumer();
        new FanOutProducer("new");

        System.out.println("After" + dataHolderList.size());
        consoleInterface();
    }

    private static void consoleInterface() throws IOException, TimeoutException {
        while (true) {
            boolean state = menuSelection();
            if (state == true) continue;
            else break;
        }

        ConnectionManager.getConnection().close();
        System.out.println("Exiting Session...");
        System.out.println("Connection Close!");
        System.out.println("Good Bye!");
    }

    private static void menuApp() {
        System.out.println("=================================");
        System.out.println("|           APP MENU            |");
        System.out.println("=================================");
        System.out.println("|Options:                        |");
        System.out.println("|       1.Publish to blogs       |");
        System.out.println("|       2.Subscribe to blog      |");
        System.out.println("|       3.Create new blog        |");
        System.out.println("|       4.Unsubscribe from blog  |");
        System.out.println("=================================");
        System.out.println("INFO: Enter 'q' to stop session");
        System.out.println("Please enter your choice");
        System.out.println("Your Choice :");
    }

    private static boolean menuSelection() throws IOException, TimeoutException {
        menuApp();
        menuChoice = 0;
        String input = scanner.nextLine();
        if ("q".equalsIgnoreCase(input)) {
            return false;
        } else if (checkIfDigit(input)) menuChoice = Integer.parseInt(input);
        else System.out.println("Invalid Command!");

        if (menuChoice == 1) {
            //publishBlogs
            DataHolder selectedBlog = getDataHolder();
            if(selectedBlog != null) Producer.publishMessage(selectedBlog);
        } else if (menuChoice == 2) {
            //subscribeToBlogs
            DataHolder selectedTag = getDataHolder();
            if(selectedTag != null) Consumer.subscribeMessage(selectedTag);
        } else if (menuChoice == 3) {
            //createBlogs
            createBlogs();
            new FanOutProducer();
        } else if (menuChoice == 4) {
            //unsubscribeBlogs
            DataHolder selectedTag = getDataHolder();
            if(selectedTag != null) Consumer.unsubscribeBlogs(selectedTag);
        }
        return true;
    }

    private static void createBlogs() throws IOException {
        System.out.println(" [x] Please enter a suitable name for your blog:");
        String blogName = scanner.nextLine();
        System.out.println(" [x] Please add tags with spaces related to '" + blogName + "':");
        String tag = scanner.nextLine();
        DataHolder object = new DataHolder(blogName,tag);
        Channel channel = ConnectionManager.getConnection().createChannel();
        channel.queueDeclare(object.getQueueName(), false, false, true, null);
        channel.queueBind(object.queueName, EXCHANGE_TOPIC, object.routingKey);
        dataHolderList.add(object);
        new FanOutProducer();
    }

    private static DataHolder getDataHolder() {
        while(true){
            System.out.println("|Select topics          |");
            for (int i = 0; i < dataHolderList.size(); i++) {
                System.out.println("|       " + (i + 1) + "." + (
                        menuChoice != 1 ?
                        dataHolderList.get(i).getRoutingKey() :
                        dataHolderList.get(i).getQueueName()) + "       ");
            }

            System.out.println("INFO: Enter 'q' to go back to menu");

            System.out.println("Please enter your choice");

            System.out.println("Your Choice :");

            String input = scanner.nextLine();

            if (checkIfDigit(input)) {

                choice = Integer.parseInt(input);

                if (choice > dataHolderList.size()) {
                    System.out.println("Invalid Command!");
                    continue;
                }
                choice = Integer.parseInt(input);
                DataHolder dataHolder = dataHolderList.get(choice - 1);
                return dataHolder;
            }else if ("q".equalsIgnoreCase(input)) {
                break;
            } else {
                System.out.println("Invalid Command!");
                continue;
            }
        }
        return null;

    }



    public static void declareQueues() throws IOException {
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
