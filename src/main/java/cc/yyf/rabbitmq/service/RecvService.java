package cc.yyf.rabbitmq.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RecvService {

    @RabbitListener(queues = "queue_yyf")
    public void recv(String msg) {
        System.out.println("收到消息: " + msg);
    }
}
