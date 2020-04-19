package com.lxh.shequ.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: shequ
 * @description: 帖子讨论
 * @author: KaiDo
 * @return:
 * @create: 2020-04-08 14:00
 **/
@Component
@Getter
@Setter
@ToString
public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type; //0普通 1置顶
    private int status; //0正常 1精华 2拉黑
    private Date createTime;
    private int commentCount;
    private double score;

}
