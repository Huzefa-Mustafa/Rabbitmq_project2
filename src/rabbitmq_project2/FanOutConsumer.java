package rabbitmq_project2;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

import static rabbitmq_project2.Declarations.EXCHANGE_FANOUT;
import static rabbitmq_project2.Main.dataHolderList;

public class FanOutConsumer {

    public FanOutConsumer() {
        try {
            //Creating connection to the server
            Channel channel = ConnectionManager.getConnection().createChannel();

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_FANOUT, "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
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
                    Set<DataHolder> s = new HashSet<>(dataHolderList);
                    dataHolderList = new ArrayList<>();
                    dataHolderList.addAll(s);
                }
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
