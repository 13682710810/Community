package com.lxh.shequ.entity;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: shequ
 * @description: Kafka事件封装
 * @author: KaiDo
 * @return:
 * @create: 2020-04-13 17:46
 **/
@Component
public class Event {

    private String topic;

    private int userId; //谁触发的主题,谁发送的消息

    private int entityType; //主题类型

    private int entityId;   //主题Id

    private int entityUserId; //主题(帖子/评论)作者，接收消息

    //未来拓展，用来存储其他业务
    private Map<String,Object> data=new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
