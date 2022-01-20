package rabbitmq_project2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class Main {
    static List<DataHolder> dataHolderList = new ArrayList<>();

    public static void main(String[] args) throws IOException, TimeoutException {
        Declarations.declareExchange();
        PreLoadQueues.preLoadQueues();
        Declarations.declareQueues();
        new FanOutConsumer();
        new FanOutProducer("new");
        new ConsoleInterface();
    }
}
