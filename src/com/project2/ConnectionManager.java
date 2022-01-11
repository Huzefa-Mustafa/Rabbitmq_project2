package com.project2;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
public class ConnectionManager {
    private static Connection connection = null;
    /**
     * Create RabbitMQ Connection
     *
     * @return Connection
     */
    public static Connection getConnection(){
        if (connection == null) {
            try {
                //Creating a connection to the server
                ConnectionFactory factory = new ConnectionFactory();
                // Inserting the data of RabbitMQ administration account
                factory.setUsername("studentx");
                factory.setPassword("studentx");

                // Inserting the IP of the machine where the server is running
                factory.setHost("127.0.0.1");
                factory.setPort(5672);
                connection = factory.newConnection();// connection interface used to open channel
            } catch (IOException | TimeoutException e) {
                System.out.println("Connection to Server is lost!!!!");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
