package com.lxh.shequ.event;

import com.alibaba.fastjson.JSONObject;
import com.lxh.shequ.entity.Event;
import com.lxh.shequ.entity.Message;
import com.lxh.shequ.service.MessageService;
import com.lxh.shequ.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: shequ
 * @description: 消费者
 * @author: KaiDo
 * @return:
 * @create: 2020-04-13 22:09
 **/
@Component
public class EventConsumer implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_FOLLOW,TOPIC_LIKE})
    public void handlerMessage(ConsumerRecord record){
        if(record==null || record.value()==null){   //判断消息是否为空
            logger.error("消息的内容为空！");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(),Event.class);
        if(event == null){  //将JSON转换为Event后，如果为空，记录日志
            logger.error("消息格式错误！");
            return;
        }
        //发送站内通知,取出生产者的消息，构造message对象
        Message message = new Message();
        message.setFromId(SYSTEM_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic()); //将之前的112_113改为类型(Topic)，因为是系统通知，左边永远是1
        message.setCreateTime(new Date());

        //Content存具体的内容
        Map<String,Object> content = new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        //event的data存放额外的数据。
        //判断对象是否为空
        if(!event.getData().isEmpty()){
            //遍历event的data(其他数据)
            for(Map.Entry<String,Object> entry :event.getData().entrySet()){
                content.put(entry.getKey(),entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);


    }

}
