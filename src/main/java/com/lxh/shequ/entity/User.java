package com.lxh.shequ.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: shequ
 * @description:
 * @author: KaiDo
 * @return:
 * @create: 2020-04-08 11:54
 **/
@Getter
@Setter
@ToString
@Component
public class User {

    private int id;

    private String username;

    private String password;

    private String salt;

    private String email;

    private int type; //1超级管理 0普通用户

    private int status; // 激活状态 1是激活 0未激活

    private String activationCode; //激活码

    private String headerUrl; // 头像路径

    private Date createTime;
}
