package com.lxh.shequ.controller;
import com.lxh.shequ.entity.DiscussPost;
import com.lxh.shequ.entity.Page;
import com.lxh.shequ.entity.User;
import com.lxh.shequ.service.DiscussPostService;
import com.lxh.shequ.service.LikeService;
import com.lxh.shequ.service.UserService;
import com.lxh.shequ.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import	java.util.function.Consumer;
import java.util.logging.Handler;

/**
 * @program: shequ
 * @description:
 * @author: KaiDo
 * @return:
 * @create: 2020-04-08 15:02
 **/
@Controller
public class IndexController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;

    // 方法调用前,SpringMVC会自动实例化Model和Page,并将Page注入Model.
    // 所以,在thymeleaf中可以直接访问Page对象中的数据.
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model,Page page) {
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for (DiscussPost post : list) {
                Map<String,Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                //页面显示赞数量
                Long likeCount=likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                map.put("likeCount",likeCount);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "index";
    }

    @RequestMapping(path = "/error",method = RequestMethod.GET)
    public String getError(){
        return "/error/500";
    }
}
