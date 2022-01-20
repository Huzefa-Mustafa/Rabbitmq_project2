package rabbitmq_project2;

import java.io.IOException;

import static rabbitmq_project2.Main.dataHolderList;

public class PreLoadQueues {

    static void preLoadQueues() throws IOException {


        DataHolder europe = new DataHolder("Europe");
        europe.addRkToList("europe.#");
        europe.addRkToList("europe.tourism.berlin");
        europe.addRkToList("europe.germany.berlin");
        europe.addRkToList("*.tourism.*");
        europe.addRkToList("*.*.berlin");
        dataHolderList.add(europe);

        DataHolder asia = new DataHolder("Asia");
        asia.addRkToList("asia.#");
        asia.addRkToList("asia.tourism.karachi");
        asia.addRkToList("asia.pakistan.karachi");
        asia.addRkToList("*.tourism.*");
        asia.addRkToList("*.*.karachi");
        dataHolderList.add(asia);



        Declarations.declareQueues();

    }
}
