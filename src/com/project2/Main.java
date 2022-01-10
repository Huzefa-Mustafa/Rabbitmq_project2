package com.project2;
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
}
