package rabbitmq_project2;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static rabbitmq_project2.ConsoleInterface.scanner;
import static rabbitmq_project2.Declarations.EXCHANGE_TOPIC;

public class TopicProducer {

    public static void publishMessage(DataHolder dataHolder) throws IOException, TimeoutException {
        System.out.println(" [x] Please enter your message related to '" + dataHolder.queueName + "':");
        String input = scanner.nextLine();

        try(Channel channel = ConnectionManager.getConnection().createChannel()){
            channel.basicPublish(EXCHANGE_TOPIC, dataHolder.routingKey, null, input.getBytes());
            System.out.println(" [x] Sent '" + dataHolder.routingKey + "':'" + input + "'");
        }
    }
}
