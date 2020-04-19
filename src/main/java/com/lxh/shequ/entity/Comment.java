package com.lxh.shequ.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: shequ
 * @description: 评论
 * @author: KaiDo
 * @return:
 * @create: 2020-04-08 14:00
 **/
@Component
@Getter
@Setter
@ToString
public class Comment {

    private int id;
    private int userId;
    private int entityType;
    private int entityId; //1帖子 2回复
    private int targetId;
    private String content;
    private int status;
    private Date createTime;
}
