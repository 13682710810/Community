package com.lxh.shequ.interceptor;


import com.lxh.shequ.entity.LoginTicket;
import com.lxh.shequ.entity.User;
import com.lxh.shequ.service.UserService;
import com.lxh.shequ.util.CookieUtil;
import com.lxh.shequ.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @program: shequ
 * @description: 拦截器  Cookie,登录时拦截进行验证
 * @author: KaiDo
 * @return:
 * @create: 2020-04-09 23:53
 **/
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    //请求拦截器，在请求前拦截并获取Cookie
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从Cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");
        if(ticket != null){
            //查询凭证
           LoginTicket loginTicket = userService.findLoginTicketByTicket(ticket);
           //查询凭证是否有效
           if(loginTicket !=null && loginTicket.getStatus()==0 && loginTicket.getExpired().after(new Date())){
               //依靠凭证查询user用户
               User user = userService.findUserById(loginTicket.getUserId());
                //在本次请求中持有用户，防止浏览器多线程并发，产生冲突.
                //请求未完成，线程会一直保持。请求完成，线程销毁。
               hostHolder.setUser(user);
           }
        }
        return true;
    }

    //模板加载前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    // 在TemplateEngine之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
