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
//                    System.out.println(" [x] Received '" +
//                            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                    new FanOutProducer();
                } else {
                    DataHolder[] msg = new Gson().fromJson(message,DataHolder[].class);
                    List<DataHolder> list = Arrays.asList(msg);


                    for (DataHolder NewQueueNames : list) {
                        int i = 0;
                        for (DataHolder oldQueueNames : dataHolderList) {
                            if (NewQueueNames.getQueueName().equals(oldQueueNames.getQueueName())) {
                                for (String tagName : NewQueueNames.getList()) {
                                    oldQueueNames.addRkToList(tagName);
                                }
                                System.out.println("Printing new List");
                                System.out.println(oldQueueNames.getQueueName() + " " + oldQueueNames.getList());
                                System.out.println("After adding new list to old");
                                System.out.println(oldQueueNames.getQueueName()+" "+oldQueueNames.getList());
                            }
                        }
                        ++i;
                    }
                    for (DataHolder x : list){
                        if (!dataHolderList.contains(x))
                            dataHolderList.add(x);
                        System.out.println("Difference added from recive list");
                        System.out.println(x.getQueueName()+" "+x.getList());
                    }
                    System.out.println("Final List");
                    for (DataHolder test : dataHolderList) {
                        System.out.println("print set ");
                        System.out.println(test.getQueueName()+" "+test.getList());
                        System.out.println();
                    }
//                    dataHolderList.addAll(list);
//                    dataHolderList = list;
                   /* dataHolderList.stream()
                            .distinct()
                            .forEach(System.out::println);*/
//                    for (DataHolder test : uniqueStudentSet) {
//                        System.out.println("print set ");
//                        System.out.println(test.getQueueName()+" "+test.getList());
//                        System.out.println();
//                    }
//                    dataHolderList.addAll(uniqueStudentSet);
//                    Collections.addAll(dataHolderList, msg);
//                    Removing Duplicates;
                    Set<DataHolder> s = new HashSet<DataHolder>(dataHolderList);
                    dataHolderList = new ArrayList<DataHolder>();
                    dataHolderList.addAll(s);
                    /*for (DataHolder test : s) {
                        System.out.println("print set ");
                        System.out.println(test.getQueueName()+" "+test.getList());
                        System.out.println();
                    }*/
//                    dataHolderList = new ArrayList<DataHolder>();
//
                    //Now the List has only the identical Elements



                }

/*                for (DataHolder dataHolder : dataHolderList) {
                    System.out.println(dataHolder.getList());
*//*                    Iterator<String> crunchifyIterator = dataHolder.getList().iterator();
                    while (crunchifyIterator.hasNext()) {
                        System.out.println(crunchifyIterator.next());
                    }*//*
                    *//*for ( String listPrint : dataHolder.getList()) {
                        System.out.println(listPrint);
                    }*//*
                }
                System.out.println();
                for (DataHolder dataHolder : dataHolderList) {
                    if (dataHolder.getQueueName().equals("HealthQ")) {
                        for (String x : dataHolder.getList()) {
                            System.out.println(x);
                        }
                    }
                }*/
//                new FanOutProducer();
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
