package com.example.rabbitmq.MultiWorksTest;

import com.example.rabbitmq.CommonProps;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * 1.如果我们用的autoAck = true，那么queue里面的数据会按顺序一次发送给不同的consumer，
 * 就算其中一个处理了很长时间，也会等着它完成后再把该发送给它的那些数据发送给它。
 * 同时，如果一个consumer挂了，那么那些积攒的理应发送给它的数据就会丢失掉。（如果
 * 在一个consumer挂掉之后才有新数据，那么这个数据是不会被标注给这个consumer的，这样不会丢失）。
 *
 * 2.与1对立，只要我们做了autoAck=false（默认是这个）【注意，此时我们需要自己手动编写ack的代码】，
 * 那么我们就不会出现丢失了，那些积攒的理应给它的数据会被分配给别的可用的consumer。
 */
public class MultiWorksProducer {
    private static final Logger logger = LoggerFactory.getLogger(MultiWorksConsumer1.class);

    private final static String quque_name = "queues";
    private final static String type = "auto";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = CommonProps.getConnectionFactory();

        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(quque_name, false, false, false, null);

            if (type.equals("in")) {
                while (true) {
                    InputStream inputStream = System.in;
                    byte[] bytes = new byte[1024];
                    inputStream.read(bytes);
                    int i = Integer.parseInt(new String(bytes, "utf-8").trim());
                    String message = "This is " + i + " " + getDots(i) + " message".join(" ");
                    channel.basicPublish("", quque_name, null, message.getBytes());
                    System.out.println(" [x] Sent '" + message + "'");
                }
            } else {
                for (int i = 1; i <= 10; i++) {
                    String message = "This is " + i + " " + getDots(i) + " message".join(" ");
                    channel.basicPublish("", quque_name, null, message.getBytes());
                    System.out.println(" [x] Sent '" + message + "'");
                }
                channel.close();
                connection.close();
                System.out.println("exit ...");
            }




        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static String getDots(int i) {
        String s = "";
        for (int j = 0; j < i; j++) {
            s += ".";
        }

        return s;
    }

}
