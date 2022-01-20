package rabbitmq_project2;

import com.rabbitmq.client.Channel;

import java.io.IOException;

import static rabbitmq_project2.Main.dataHolderList;

public class Declarations {

    static final String EXCHANGE_TOPIC = "topic_logs";
    static final String EXCHANGE_FANOUT = "logs";

    static void declareExchange() throws IOException {
        Channel channel = ConnectionManager.getConnection().createChannel();
        //Create Topic Exchange
        channel.exchangeDeclare(EXCHANGE_TOPIC, "topic");
        channel.exchangeDeclare(EXCHANGE_FANOUT, "fanout");
    }


    static void declareQueues() throws IOException {
        //Create a channel - do not share the Channel instance
        Channel channel = ConnectionManager.getConnection().createChannel();
        //Create the Queues
        for(DataHolder dataHolder : dataHolderList){
            channel.queueDeclare(dataHolder.getQueueName(), true, false, false, null);
            for ( String routingKey : dataHolder.getList()) {
                //Create bindings - (queue, exchange, routingKey) - routingKey != null
                channel.queueBind(dataHolder.queueName, EXCHANGE_TOPIC, routingKey);
            }
        }
    }
}
