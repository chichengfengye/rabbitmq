package com.example.rabbitmq.simplelestTest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * 从控制台监听输入值，并publish
 * 输入“exit”后，退出程序
 */
public class SimplelestProducer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.42.6");
        factory.setPort(5672);
        factory.setPassword("jf");
        factory.setUsername("jf");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            while (true) {
                InputStream inputStream =System.in;
                byte[] bytes = new byte[1024];
                inputStream.read(bytes);

                String message = new String(bytes, "utf-8");
                if (message.trim().equalsIgnoreCase("exit")) {
                    channel.close();
                    connection.close();
                    throw new Exception("exit！");
                }

                channel.basicPublish("", QUEUE_NAME, null, bytes);
                System.out.println(" [x] Sent '" + message + "'");
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
