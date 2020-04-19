package com.lxh.shequ.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @program: shequ
 * @description:登录凭证
 * @author: KaiDo
 * @return:
 * @create: 2020-04-08 14:01
 **/
@Getter
@Setter
@ToString
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket; //凭证
    private int status; //0有效，1无效
    private Date expired; //期满时间

}
