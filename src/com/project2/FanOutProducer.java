package com.project2;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.project2.Main.EXCHANGE_FANOUT;
import static com.project2.Main.dataHolderList;

public class FanOutProducer {
    ConnectionFactory factory = new ConnectionFactory();
    public FanOutProducer() {
        //Inserting data of our RabbitMQ administration account
        factory.setUsername("studentx");
        factory.setPassword("studentx");

        //Inserting the IP of a server where machine is running
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
//        System.out.println(dataHolderList.size());
        //Creating connection to the server
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_FANOUT, "fanout");
            String msg = new Gson().toJson(dataHolderList);
            channel.basicPublish(EXCHANGE_FANOUT, "", null, msg.getBytes());
//            System.out.println("[x] Sent '" + msg + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public FanOutProducer(String s) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_FANOUT, "fanout");
            channel.basicPublish(EXCHANGE_FANOUT, "", null, s.getBytes());
//            System.out.println("[x] Sent '" + s + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
