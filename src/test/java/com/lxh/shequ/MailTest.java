package com.lxh.shequ;

import com.lxh.shequ.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @program: shequ
 * @description: 邮件测试
 * @author: KaiDo
 * @return:
 * @create: 2020-04-08 22:43
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ShequApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void sendTextTest(){

        mailClient.sendMail("2061794210@qq.com","你好","good");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        //在HTML文件里获取"username"
        context.setVariable("username","madaha");
        //邮件内容
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);

        mailClient.sendMail("2061794210@qq.com","html",content);

    }
}
