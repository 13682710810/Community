package com.lxh.shequ;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @program: shequ
 * @description: kafka测试
 * @author: KaiDo
 * @return:
 * @create: 2020-04-13 15:14
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ShequApplication.class)
public class KafkaTest {
}
