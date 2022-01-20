package rabbitmq_project2;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import java.io.IOException;

import static rabbitmq_project2.Declarations.EXCHANGE_FANOUT;
import static rabbitmq_project2.Main.dataHolderList;

public class FanOutProducer {
    public FanOutProducer() throws IOException {
        //Creating connection to the server
        Channel channel = ConnectionManager.getConnection().createChannel();
            String msg = new Gson().toJson(dataHolderList);
            channel.basicPublish(EXCHANGE_FANOUT, "", null, msg.getBytes());
    }

    public FanOutProducer(String s) throws IOException {
        //Creating connection to the server
        Channel channel = ConnectionManager.getConnection().createChannel();
            channel.basicPublish(EXCHANGE_FANOUT, "", null, s.getBytes());
    }
}
