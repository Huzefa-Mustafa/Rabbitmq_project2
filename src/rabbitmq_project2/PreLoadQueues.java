package rabbitmq_project2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static rabbitmq_project2.Main.dataHolderList;

public class PreLoadQueues {

    static void preLoadQueues() throws IOException {
        List<String> queueRoutingKeys = new ArrayList<>();
        String queueName = "Health";
        DataHolder dhealth = new DataHolder(queueName,queueRoutingKeys);
        dhealth.addRkToList("health.#");
        dhealth.addRkToList("health.insurance");
        dhealth.addRkToList("*.insurance");
        dhealth.setRoutingKey("TK.insurance");
        dataHolderList.add(dhealth);
        Declarations.declareQueues();

    }
}
