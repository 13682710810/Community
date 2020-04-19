package com.lxh.shequ.dao;

import com.lxh.shequ.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

//不推荐使用，更改为用Redis
@Mapper
@Deprecated
public interface LoginTicketMapper {

    //插入凭证
    @Insert({"insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"})
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    //查找凭证
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectTicket(String Ticket);

    //更改状态码
    @Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
            "<if test=\"ticket!=null\"> ",   // \"为转义字符
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket,int status);//注意参数要与SQL语句的一致。大小写要一致！！！

}
