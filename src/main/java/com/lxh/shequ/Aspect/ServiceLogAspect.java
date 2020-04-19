package com.lxh.shequ.Aspect;
import java.time.LocalDate;
import java.time.LocalDateTime;
import	java.time.LocalTime;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @program: shequ
 * @description: aop日志记录
 * @author: KaiDo
 * @return:
 * @create: 2020-04-11 18:08
 **/
@Component
@Aspect
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.lxh.shequ.service.*.*(..))")
    public void pointCut(){}

    @Before("pointCut()")
    public void brfore(JoinPoint joinPoint){
        // 用户[???],在[xxx],访问了[com.nowcoder.community.service.xxx()].
        //如何获得request对象，不能在方法中加上参数request。
        //进行强转，ServletRequestAttributes的功能比较多
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //attributes有可能为空，因为Kafka的消费者类调用了Service，而不是通过Controller调用
        if(attributes==null){
            return; //不记录日志
        }
        HttpServletRequest request = attributes.getRequest();
        //远程主机IP
        String ip =request.getRemoteHost();
//        String nowTime= new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalTime time = LocalTime.now();
//        String date = time.format(formatter);
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //第一个为类名xxx，第二个为方法
        String target=joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        //某用户在某时间访问了某方法
        logger.info(String.format("用户[%s]在[%s]访问了[%s]方法",ip,nowTime,target));


    }
}
