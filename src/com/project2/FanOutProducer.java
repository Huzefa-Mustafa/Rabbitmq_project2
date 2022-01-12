package com.project2;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.project2.Main.dataHolderList;

public class FanOutProducer {
    private static final String EXCHANGE_TOPIC = "topic_logs";
    private static final String EXCHANGE_FANOUT = "logs";
    public FanOutProducer() throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();

        //Inserting data of our RabbitMQ administration account
        factory.setUsername("studentx");
        factory.setPassword("studentx");

        //Inserting the IP of a server where machine is running
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        System.out.println(dataHolderList.size());
        //Creating connection to the server
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_FANOUT, "fanout");
            String msg = new Gson().toJson(dataHolderList);
            channel.basicPublish(EXCHANGE_FANOUT, "", null, msg.getBytes());
            System.out.println("[x] Sent '" + msg + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

//    public static void createBlogs(String blogName, String tag) throws IOException, TimeoutException {
//
//        ConnectionFactory factory = new ConnectionFactory();
//
//        //Inserting data of our RabbitMQ administration account
//        factory.setUsername("studentx");
//        factory.setPassword("studentx");
//
//        //Inserting the IP of a server where machine is running
//        factory.setHost("127.0.0.1");
//        factory.setPort(5672);
//        try (Connection connection = factory.newConnection();
//             Channel channel = connection.createChannel()) {
//            System.out.println("producer");
//            channel.exchangeDeclare(EXCHANGE_FANOUT, "fanout");
//            //Creating connection to the server
//            TopicExchange.declareExchange();
//            channel.queueDeclare(blogName, true, false, false, null);
//            channel.queueBind(blogName, EXCHANGE_TOPIC, tag);
//            channel.exchangeDeclare(EXCHANGE_FANOUT, "fanout");
//            String msg = new Gson().toJson(dataHolderList);
//            channel.basicPublish(EXCHANGE_FANOUT, "", null, msg.getBytes());
//            System.out.println("[x] Sent '" + msg + "'");
//        }
//    }
}
