package rabbitmq_project2;



import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

import static rabbitmq_project2.Declarations.EXCHANGE_TOPIC;


public class TopicConsumer {

    private static Channel channel;

    public static void subscribeMessage(DataHolder dataHolder) throws IOException {

//        String queueName = Declarations.declareQueues(dataHolder.getRoutingKey());
        channel = ConnectionManager.getConnection().createChannel();
//        channel = ConnectionManager.getConnection().createChannel();
        String bindingKey = dataHolder.getRoutingKey();
        String queueName = channel.queueDeclare().getQueue();

        //Create bindings - (queue, exchange, routingKey) - routingKey != null
        channel.queueBind(queueName, EXCHANGE_TOPIC, bindingKey);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("\n\n=========== "+ dataHolder.getRoutingKey() +" Tags ==========");
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            dataHolder.setConsumerTag(consumerTag);
        };
        CancelCallback cancelCallback = consumerTag -> { };
        channel.basicConsume(queueName, true,deliverCallback, cancelCallback);
    }

    public static void unsubscribeBlogs(DataHolder dataHolder)  {
        try {
            channel.basicCancel(dataHolder.getConsumerTag());
        } catch (IOException e) {
            System.out.println("Unsubscribed");
        }

    }
}
