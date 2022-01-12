package com.project2;


public class DataHolder {
    String queueName;
    String routingKey;
    String consumerTag;

    public DataHolder(String queueName, String routingKey) {
        this.queueName = queueName;
        this.routingKey = routingKey;
    }


    public String getQueueName() {
        return queueName;
    }
    public String getConsumerTag() {
        return consumerTag;
    }
    public void setConsumerTag(String consumerTag) {
        this.consumerTag = consumerTag;
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

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof DataHolder)
        {
            DataHolder temp = (DataHolder) obj;
            if(this.queueName.equals(temp.queueName) && this.routingKey.equals(temp.routingKey))
                return true;
        }
        return false;
    }
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub

        return (this.queueName.hashCode() + this.routingKey.hashCode());
    }

}
