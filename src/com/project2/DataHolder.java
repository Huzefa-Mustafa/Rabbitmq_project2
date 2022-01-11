package com.project2;

import java.util.ArrayList;

public class DataHolder {
    String queueName;
    String routingKey;

    public DataHolder(String queueName, String routingKey) {
        this.queueName = queueName;
        this.routingKey = routingKey;
    }
    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
//    public void addDataHolderToList(DataHolder dataHolder) {
//        this.dataHolderList.add(dataHolder);
//    }
//    public ArrayList<DataHolder> getDataHolderFromList(){
//        return this.dataHolderList;
//    }
}
