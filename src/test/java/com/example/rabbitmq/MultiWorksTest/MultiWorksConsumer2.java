package com.example.rabbitmq.MultiWorksTest;

import com.example.rabbitmq.CommonProps;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 1.如果我们用的autoAck = true，那么queue里面的数据会按顺序一次发送给不同的consumer，
 * 就算其中一个处理了很长时间，也会等着它完成后再把该发送给它的那些数据发送给它。
 * 同时，如果一个consumer挂了，那么那些积攒的理应发送给它的数据就会丢失掉。（如果
 * 在一个consumer挂掉之后才有新数据，那么这个数据是不会被标注给这个consumer的，这样不会丢失）。
 * <p>
 * 2.与1对立，只要我们做了autoAck=false（默认是这个）【注意，此时我们需要自己手动编写ack的代码】，
 * 那么我们就不会出现丢失了，那些积攒的理应给它的数据会被分配给别的可用的consumer。
 */
public class MultiWorksConsumer2 {
    private static final Logger logger = LoggerFactory.getLogger(MultiWorksConsumer2.class);

    private final static String QUEUE_NAME = "queues";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory connectionFactory = CommonProps.getConnectionFactory();

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                System.out.println(" [x] Done");
            }
        };

        boolean autoAck = false; // acknowledgment is covered below
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
        });
    }

    private static void doWork(String task) throws InterruptedException {
        Thread.sleep(5000);
    }
}
