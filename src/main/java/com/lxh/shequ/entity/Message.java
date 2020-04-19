package com.lxh.shequ.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @program: shequ
 * @description: 消息会话
 * @author: KaiDo
 * @return:
 * @create: 2020-04-11 09:52
 **/
@Getter
@Setter
@ToString
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;
}
