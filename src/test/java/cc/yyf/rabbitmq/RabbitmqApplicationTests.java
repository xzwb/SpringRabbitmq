package cc.yyf.rabbitmq;


import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 将数据放入索引
     * @throws IOException
     */
    @Test
    void index() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "yyf");
        map.put("age", 15);
        map.put("note", "嘿嘿嘿不知道");
        IndexRequest indexRequest = new IndexRequest("yy").id("1").source(map);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse);
    }

    /**
     * 获取数据
     * @throws IOException
     */
    @Test
    void getRequest() throws IOException {
        GetRequest getRequest = new GetRequest("yy", "1");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> map  = getResponse.getSourceAsMap();
        System.out.println(map);
    }

    /**
     * 存在
     */
    @Test
    void exists() throws IOException {
        GetRequest getRequest = new GetRequest("yy", "1");
        /**
         * 禁止获取source
         */
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        /**
         * 禁止获取存储的字段
         */
        getRequest.storedFields("_none_");
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 删除
     */
    @Test
    void delete() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("yyf", "2");
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    /**
     * 更新
     */
    @Test
    void update() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "xy");
        map.put("note", "嘿嘿嘿就是不知道");
        map.put("id", 5);
        UpdateRequest updateRequest = new UpdateRequest("yy", "1").doc(map);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
    }
}
