package cc.yyf.rabbitmq;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class RabbitmqApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 点对点消息
     */
    @Test
    void contextLoads() {
//        可以定制消息内容和消息体
//        rabbitTemplate.send(exchange, routeKey, message);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "这是第一个消息");
        map.put("data", Arrays.asList("helloWorld", 123, true));
        // 对象被默认序列化后发出
        rabbitTemplate.convertAndSend("exchange_yyf", "info", map);
    }

    @Test
    void recv() {
        Map<String, Object>  map = (Map<String, Object>) rabbitTemplate.receiveAndConvert("queue_yyf");
        System.out.println(map);
    }

    @Test
    void send() {
        rabbitTemplate.convertAndSend("exchange_yyf", "info", "15389237357");
    }


}
