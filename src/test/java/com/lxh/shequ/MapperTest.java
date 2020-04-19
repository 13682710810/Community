package com.lxh.shequ;

import com.lxh.shequ.dao.DiscussPostMapper;
import com.lxh.shequ.dao.UserMapper;
import com.lxh.shequ.entity.DiscussPost;
import com.lxh.shequ.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @program: shequ
 * @description:
 * @author: KaiDo
 * @return:
 * @create: 2020-04-08 12:09
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ShequApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void selectDiscussPost(){
        List<DiscussPost> dcp = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for (DiscussPost discussPost : dcp) {
            System.out.println(discussPost);
        }

    }

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);
    }
    @Test
    public void testInsertUser(){
            User user = new User();
            user.setUsername("test");
            user.setPassword("123456");
            user.setSalt("abc");
            user.setEmail("test@qq.com");
            user.setHeaderUrl("http://www.nowcoder.com/101.png");
            user.setCreateTime(new Date ());

            int rows = userMapper.insertUser(user);
            System.out.println(rows);
            System.out.println(user.getId());
        }
    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "hello");
        System.out.println(rows);
    }

}
