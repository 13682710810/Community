package com.lxh.shequ;

import com.lxh.shequ.util.CommunityUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShequApplicationTests {

    @Test
    public void contextLoads() {
        CommunityUtil communityUtil = new CommunityUtil();
        String s = communityUtil.md5("123456abc");
        System.out.println(s);

    }
    @Test
    public void tset(){
        HashMap<String,Object> hashMap = new HashMap<>();
        Object o = hashMap.get("0");
        System.out.println(o);
    }

}
