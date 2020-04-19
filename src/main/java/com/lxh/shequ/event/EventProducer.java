package com.lxh.shequ.event;
import	java.net.Authenticator;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.lxh.shequ.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @program: shequ
 * @description: 生产者
 * @author: KaiDo
 * @return:
 * @create: 2020-04-13 21:38
 **/
@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    //处理事件(即发送消息)
    public void fireEvent(Event event){
        //将事件发送到指定的主题
        //data中转化为JSON发送
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));

    }
}
