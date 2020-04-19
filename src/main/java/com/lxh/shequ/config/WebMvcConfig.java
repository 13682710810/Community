package com.lxh.shequ.config;

import com.lxh.shequ.interceptor.AlphaInterceptor;
import com.lxh.shequ.interceptor.LoginRequiredInterceptor;
import com.lxh.shequ.interceptor.LoginTicketInterceptor;
import com.lxh.shequ.interceptor.MessageIntecreptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: shequ
 * @description:  配置拦截器
 * @author: KaiDo
 * @return:
 * @create: 2020-04-09 22:26
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageIntecreptor messageIntecreptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器 测试
        registry.addInterceptor(alphaInterceptor)
                //不拦截路径
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
                .addPathPatterns("/register", "/login"); //拦截路径 注册/登录

        //登录凭证
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
        //需要登录才能访问的模块
        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        //总消息拦截器，拦截所有动态请求
        registry.addInterceptor(messageIntecreptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
