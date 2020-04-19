package com.lxh.shequ.controller.advice;

import com.lxh.shequ.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: shequ
 * @description: 统一异常扫描
 * @author: KaiDo
 * @return:
 * @create: 2020-04-11 16:31
 **/
//该注解只扫描Controller
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger=LoggerFactory.getLogger(ExceptionAdvice.class);

    //表示处理所有异常
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //记录日志
        //异常概况
        logger.error("服务器发送异常:"+e.getMessage());
        //遍历异常栈的信息
        for(StackTraceElement element :e.getStackTrace()){
            logger.error(element.toString());
        }

        //判断请求是页面请求，还是异步请求(JSON)
        //从消息头里查看请求方式
        String xRequestedWith = request.getHeader("x-requested-with");
        //异步请求：访问的是XML，返回的可以是JSON
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            //写json的话，会向浏览器返回字符串。
            //如果写palin,会向浏览器返回一个普通的字符串，但是浏览器需要将字符串转化为json
            //data = $.parseJSON(data); 将字符串转化为json
            //response.setContentType("application/json;charset=utf-8");
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "服务器异常!"));
        } else {
            //普通请求页面
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
