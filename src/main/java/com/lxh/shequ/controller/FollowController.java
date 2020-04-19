package com.lxh.shequ.controller;
import	java.security.KeyStore.Entry.Attribute;
import java.util.List;
import java.util.Map;

import com.lxh.shequ.entity.Event;
import com.lxh.shequ.entity.Page;
import com.lxh.shequ.entity.User;
import com.lxh.shequ.event.EventProducer;
import com.lxh.shequ.service.FollowService;
import com.lxh.shequ.service.UserService;
import com.lxh.shequ.util.CommunityConstant;
import com.lxh.shequ.util.CommunityUtil;
import com.lxh.shequ.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: shequ
 * @description: 关注
 * @author: KaiDo
 * @return:
 * @create: 2020-04-12 16:41
 **/
@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer Producer;

    //在拦截器拦截该方法，防止没登录的用户操作
    //entityId在前端由隐藏域结合JS传到后台
    @RequestMapping(path = "/follow",method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityId,int entityType){
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);
        //触发关注事件
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(hostHolder.getUser().getId())  //关注者：当前用户
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);     //被关注者，此处为用户，未来有新的再改
        Producer.fireEvent(event);

        return CommunityUtil.getJSONString(0, "已关注！");
    }

    @RequestMapping(path = "/unfollow",method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType,int entityId){
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);
        return CommunityUtil.getJSONString(0, "已取消关注！");
    }

    /**
    * @Description: 关注列表 分页
    */
    @RequestMapping(path = "/followees/{userId}", method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);
        //分页设置
        page.setLimit(5);
        page.setPath("/followees/" + userId);
        //一共多少行数据，
        page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));
        //分页查询关注列表
        List<Map<String, Object>> userList = followService.findFollowee(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                //hasFollowed()判断是否是登录的用户 & 如果登录，判断是否关注过
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);
        return "/site/followee";
    }

    /**
    * @Description: 粉丝列表 分页
    */
    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);
        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER, userId));
        //分页查询粉丝列表
        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");

                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);

        return "/site/follower";
    }
    //判断是否是登录的用户 & 如果登录，判断是否关注过
    private boolean hasFollowed(int userId) {
        if (hostHolder.getUser() == null) {
            return false;
        }

        return followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
    }
}
