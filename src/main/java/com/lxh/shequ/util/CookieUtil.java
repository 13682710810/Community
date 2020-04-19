package com.lxh.shequ.util;

import org.springframework.http.HttpRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: shequ
 * @description: Cookie封装类，给拦截器复用
 * @author: KaiDo
 * @return:
 * @create: 2020-04-10 00:02
 **/
public class CookieUtil {
    //request获取cookie,name是所要获取的对象的key；cookie在LoginController中存入。
    public static String getValue(HttpServletRequest request,String name){
        if (request == null || name == null) {
            throw new IllegalArgumentException("参数为空!");
        }
        Cookie[] cookies = request.getCookies();

        if(cookies!=null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
