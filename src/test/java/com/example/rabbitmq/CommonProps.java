package com.example.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;

public class CommonProps {
    public static final String host = "192.168.42.6";
    public static final int port = 5672;
    public static final String username = "jf";
    public static final String password = "jf";

    public static ConnectionFactory getConnectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(CommonProps.host);
        connectionFactory.setPort(CommonProps.port);
        connectionFactory.setUsername(CommonProps.username);
        connectionFactory.setPassword(CommonProps.password);

        return connectionFactory;
    }
}
