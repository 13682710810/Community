package com.lxh.shequ.interceptor;

import com.lxh.shequ.annotation.LoginRequired;
import com.lxh.shequ.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @program: shequ
 * @description: 拦截自定义注解注解的方法
 * @author: KaiDo
 * @return:
 * @create: 2020-04-10 12:41
 **/
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {



    @Autowired
    private HostHolder hostHolder;

    //拦截目标Object handler
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            //访问当前方法需要登录，如果没登录跳转登录页面
            if (loginRequired != null && hostHolder.getUser() == null) {
                //不能直接返回模板页面
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
