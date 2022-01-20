package rabbitmq_project2;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataHolder {
    String queueName;
    String routingKey;
    String consumerTag;
    List<String> list;


    public DataHolder(String queueName, List<String> list) {
        this.queueName = queueName;
        this.list = list;
    }

    public List<String> getList() {
        removeDoublingTags();
        //Removing Duplicates;

        return list;
    }

    public void addRkToList(String rk) {
        list.add(rk);
        removeDoublingTags();
    }

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

    public void removeDoublingTags() {
        //Removing Duplicates;
        Set<String> s = new HashSet<String>(list);
        list = new ArrayList<String>();
        list.addAll(s);
        //Now the List has only the identical Elements
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof DataHolder)
        {
            DataHolder temp = (DataHolder) obj;
            if (this.queueName.equals(temp.queueName)&& this.list.equals(temp.list) ) {

                return true;
            }
        }
        return false;
    }
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub

        return (this.queueName.hashCode() + this.list.hashCode());
    }

}
