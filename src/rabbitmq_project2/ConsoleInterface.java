package rabbitmq_project2;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static rabbitmq_project2.Declarations.EXCHANGE_TOPIC;
import static rabbitmq_project2.Main.dataHolderList;

public class ConsoleInterface {
    static Scanner scanner = new Scanner(System.in);
    static int choice, menuChoice,tagChoice;
    ConsoleInterface() throws IOException, TimeoutException {
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

    private static boolean menuSelection() throws IOException, TimeoutException {
        menuApp();
        String input = scanner.nextLine();
        if ("q".equalsIgnoreCase(input)) {
            return false;
        } else if (checkIfDigit(input)) menuChoice = Integer.parseInt(input);
        else System.out.println("Invalid Command!");

        if (menuChoice == 1) {
            //publishBlogs
            DataHolder selectedBlog = getDataHolder();
            if(selectedBlog != null) TopicProducer.publishMessage(selectedBlog);
        } else if (menuChoice == 2) {
            //subscribeToBlogs
            DataHolder selectedTag = getDataHolder();
            if(selectedTag != null) TopicConsumer.subscribeMessage(selectedTag);
        } else if (menuChoice == 3) {
            //createBlogs
//            createBlogs();
            if (createBlogs()) new FanOutProducer();
        } else if (menuChoice == 4) {
            //unsubscribeBlogs
            DataHolder selectedTag = getDataHolder();
            if(selectedTag != null) TopicConsumer.unsubscribeBlogs(selectedTag);
        }
        return true;
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
    private static boolean createBlogs() throws IOException {
        List<String> newListTagForBog = new ArrayList<String>();
        Boolean queueExisted = false;
        System.out.println(" [x] Please enter a suitable name for your blog (Declaring a queue):");
        System.out.println("INFO: Enter 'q' to go back to menu");
        String blogName = scanner.nextLine();
        if ("q".equalsIgnoreCase(blogName)) {
            return false;
        }
        DataHolder object =  new DataHolder(blogName,newListTagForBog);

        for (DataHolder queueNames : dataHolderList) {
            if (blogName.equals(queueNames.getQueueName())) {
                object = queueNames;
                queueExisted = true;
            }
        }
        if (queueExisted) {
            System.out.println(" [x] Please add tags related to '" + blogName + "' (Declaring routing keys):");
            System.out.println("INFO: Enter 'q' to go back to menu");
            String tag = scanner.nextLine();
            if ("q".equalsIgnoreCase(tag)) {
                return false;
            }
            object.addRkToList(tag);
            Channel channel = ConnectionManager.getConnection().createChannel();

            channel.queueDeclare(object.getQueueName(), true, false, false, null);
            for (String routingkey : object.getList()) {
                channel.queueBind(object.queueName, EXCHANGE_TOPIC, routingkey);
            }

        } else {
            System.out.println(" [x] Please add tags related to '" + blogName + "' (Declaring routing keys):");
            System.out.println("INFO: Enter 'q' to go back to menu");
            String tag = scanner.nextLine();
            if ("q".equalsIgnoreCase(tag)) {
                return false;
            }
            object = new DataHolder(blogName,newListTagForBog);
            object.addRkToList(tag);

            Channel channel = ConnectionManager.getConnection().createChannel();

            channel.queueDeclare(object.getQueueName(), true, false, false, null);
            for ( String routingkey : object.getList()) {
                channel.queueBind(object.queueName, EXCHANGE_TOPIC, routingkey);
            }
            dataHolderList.add(object);
        }
        new FanOutProducer();
        return true;
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
