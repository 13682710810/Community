package com.lxh.shequ.util;

import com.lxh.shequ.entity.User;
import com.lxh.shequ.entity.User;
import org.springframework.stereotype.Component;

/**
 *  相当于容器，持有用户信息,用于代替session对象(session不宜用太多，存太多服务器性能会变差).
 *  用户登录后会保存，用户没登录hostHolder会为空
 *  在LoginTicketInterceptor拦截器中在登录时把数据存到HostHolder中
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
