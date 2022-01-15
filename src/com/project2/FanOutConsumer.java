package com.project2;


import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.project2.Main.*;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

public class FanOutConsumer {

    public FanOutConsumer() {
        try {
            //Creating connection the server
            ConnectionFactory factory = new ConnectionFactory();

            //Inserting data of our RabbitMQ administration account
            factory.setUsername("studentx");
            factory.setPassword("studentx");

            //Inserting the IP of a server where machine is running
            factory.setHost("127.0.0.1");
            factory.setPort(5672);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_FANOUT, "fanout");

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_FANOUT, "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                if (message.equals("new")) {
                    new FanOutProducer();
                } else {
                    DataHolder[] msg = new Gson().fromJson(message, DataHolder[].class);
                    List<DataHolder> list = Arrays.asList(msg);
                    boolean queueExisted = false;
                    for (DataHolder NewData : list) {
                        for (DataHolder OldData : dataHolderList) {
                            if (NewData.getQueueName().equals(OldData.getQueueName())) {
                                for (String tagName : NewData.getList()) {
                                    OldData.addRkToList(tagName);
                                }
                            } else {
                                queueExisted = true;
                            }
                        }
                    }
                    if (queueExisted) {
                        Collections.addAll(dataHolderList, msg);
                    }
                    Set<DataHolder> s = new HashSet<DataHolder>(dataHolderList);
                    dataHolderList = new ArrayList<DataHolder>();
                    dataHolderList.addAll(s);
                }
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
