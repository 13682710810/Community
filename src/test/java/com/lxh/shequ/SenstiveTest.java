package com.lxh.shequ;

import com.lxh.shequ.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @program: shequ
 * @description:
 * @author: KaiDo
 * @return:
 * @create: 2020-04-10 15:42
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ShequApplication.class)
public class SenstiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void test(){
        String text = "吸△△△△毒，嫖△△娼，混2蛋12";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);
    }
}
