package com.project2;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.project2.Main.scanner;

public class Producer {
    private static final String EXCHANGE_TOPIC = "topic_logs";
    private static final String EXCHANGE_FANOUT = "logs";
    /**
     * Publish Messages with different routing keys.
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void publishMessage(DataHolder dataHolder) throws IOException, TimeoutException {
        System.out.println(" [x] Please enter your message related to '" + dataHolder.queueName + "':");
        String input = scanner.nextLine();

        try(Channel channel = ConnectionManager.getConnection().createChannel()){
            channel.basicPublish(EXCHANGE_TOPIC, dataHolder.routingKey, null, input.getBytes());
            System.out.println(" [x] Sent '" + dataHolder.routingKey + "':'" + input + "'");
        }
    }
}
