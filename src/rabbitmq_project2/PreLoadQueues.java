package rabbitmq_project2;

import java.io.IOException;

import static rabbitmq_project2.Main.dataHolderList;

public class PreLoadQueues {

    static void preLoadQueues() throws IOException {
//        List<String> queueRoutingKeys = new ArrayList<>();
        String queueName = "Land";
        DataHolder dLand = new DataHolder(queueName);
        dLand.addRkToList("asia.pakistan");
        dLand.addRkToList("asia.*");
        dLand.addRkToList("asia.pakistan.karachi");
        dLand.addRkToList("europe.#");
        dLand.addRkToList("europe.germany.duisburg");
        dLand.addRkToList("europe.germany");

        dataHolderList.add(dLand);
        Declarations.declareQueues();

    }
}
