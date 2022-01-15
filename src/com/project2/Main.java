package com.project2;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Main {
    static List<DataHolder> dataHolderList = new ArrayList<>();
    static List<String> newListTagForBog = new ArrayList<String>();

    static Scanner scanner = new Scanner(System.in);
    static int choice, menuChoice,tagChoice;
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

        Boolean queueExisted = false;
        System.out.println(" [x] Please enter a suitable name for your blog:");
        String blogName = scanner.nextLine();
        DataHolder object =  new DataHolder(blogName,newListTagForBog);

        for (DataHolder queueNames : dataHolderList) {
            if (blogName.equals(queueNames.getQueueName())) {
                object = queueNames;
                queueExisted = true;
            }
        }
        if (queueExisted) {
            System.out.println(" [x] Please add tags with spaces related to '" + blogName + "':");
            String tag = scanner.nextLine();
            object.addRkToList(tag);
            Channel channel = ConnectionManager.getConnection().createChannel();

            channel.queueDeclare(object.getQueueName(), false, false, true, null);
            for (String routingkey : object.getList()) {
                channel.queueBind(object.queueName, EXCHANGE_TOPIC, routingkey);
            }
            for (DataHolder list : dataHolderList) {
                System.out.println(list.getQueueName()+" "+list.getList());
            }
            new FanOutProducer();

        } else {
            System.out.println(" [x] Please add tags with spaces related to '" + blogName + "':");
            String tag = scanner.nextLine();
            object = new DataHolder(blogName,newListTagForBog);
            object.addRkToList(tag);

            Channel channel = ConnectionManager.getConnection().createChannel();

            channel.queueDeclare(object.getQueueName(), false, false, true, null);
            for ( String routingkey : object.getList()) {
                channel.queueBind(object.queueName, EXCHANGE_TOPIC, routingkey);
            }
            dataHolderList.add(object);
            new FanOutProducer();
        }
    }

    private static DataHolder getDataHolder() {
        while(true){
            System.out.println("|Select topics          |");
            for (int i = 0; i < dataHolderList.size(); i++) {
                System.out.println("|       " + (i + 1) + "." + (
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
            }else if ("q".equalsIgnoreCase(input)) {
                break;
            } else {
                System.out.println("Invalid Command!");
                continue;
            }
            DataHolder selectedTopic = dataHolderList.get(choice - 1);
            System.out.println(selectedTopic.getList().size());
            System.out.println("|Select Key for  '"+ selectedTopic.getQueueName() +"'         |");
            for (int i = 0; i < selectedTopic.getList().size(); i++) {
                System.out.println("|       " + (i + 1) + "." + (
                        selectedTopic.getList().get(i)) + "       ");
            }

            String selectedKey = scanner.nextLine();
            if (checkIfDigit(selectedKey)) {

                tagChoice = Integer.parseInt(selectedKey);

                if (tagChoice > selectedTopic.getList().size()) {
                    System.out.println("Invalid Command!");
                    continue;
                }
            }else if ("q".equalsIgnoreCase(selectedKey)) {
                break;
            } else {
                System.out.println("Invalid Command!");
                continue;
            }
            selectedTopic.setRoutingKey(selectedTopic.getList().get(tagChoice-1));
            return selectedTopic;
        }
        return null;

    }



    public static void declareQueues() throws IOException {
        List<String> testTagList = new ArrayList<String>();
        List<String> secondTagList = new ArrayList<String>();
        //Create a channel - do not share the Channel instance
        Channel channel = ConnectionManager.getConnection().createChannel();
        TopicExchange.declareExchange();

        List<String> dhealthTagList = new ArrayList<String>();
        DataHolder dhealth = new DataHolder("HealthQ",dhealthTagList);
        dhealth.addRkToList("health.*");
        dhealth.addRkToList("health.insurance");
        dhealth.addRkToList("*.insurance");
        dhealth.setRoutingKey("health.*");

/*        List<String> sportsQTagList = new ArrayList<String>();
        DataHolder sportsQ = new DataHolder("SportsQ",sportsQTagList);
        sportsQTagList.add("#.sports.*");
//        sportsQ.setRoutingKey("#.sports.*");

        List<String> educationQTagList = new ArrayList<String>();
        DataHolder educationQ = new DataHolder("EducationQ",educationQTagList);
        educationQTagList.add("#.education");
//        educationQ.setRoutingKey("#.education");

        DataHolder test = new DataHolder("Test",testTagList);
        testTagList.add("FirstKey");
        testTagList.add("SecondKey");
        testTagList.add("ThirdKey");
//        test.setRoutingKey("SecondKey");
        DataHolder second = new DataHolder("second",secondTagList);
        secondTagList.add("secondFirstKey");
        secondTagList.add("secondSecondKey");
        secondTagList.add("secondThirdKey");
//        second.setRoutingKey("secondSecondKey");*/

        dataHolderList.add(dhealth);
/*        dataHolderList.add(sportsQ);
        dataHolderList.add(educationQ);
        dataHolderList.add(test);
        dataHolderList.add(second);*/

        //Create the Queues

        for(DataHolder dataHolder : dataHolderList){
            channel.queueDeclare(dataHolder.getQueueName(), true, false, false, null);
            for ( String routingkey : dataHolder.getList()) {
                channel.queueBind(dataHolder.queueName, EXCHANGE_TOPIC, routingkey);
            }
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
