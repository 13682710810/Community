package com.lxh.shequ.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @program: shequ
 * @description:时间配置类
 * @author: KaiDo
 * @return:
 * @create: 2020-04-09 14:40
 **/
@Configuration
public class AlphaConfig {
    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
