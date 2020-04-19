package com.lxh.shequ.controller;

import com.lxh.shequ.entity.Comment;
import com.lxh.shequ.entity.DiscussPost;
import com.lxh.shequ.entity.Event;
import com.lxh.shequ.event.EventProducer;
import com.lxh.shequ.service.CommentService;
import com.lxh.shequ.service.DiscussPostService;
import com.lxh.shequ.util.CommunityConstant;
import com.lxh.shequ.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @program: shequ
 * @description: 评论
 * @author: KaiDo
 * @return:
 * @create: 2020-04-11 00:29
 **/
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer Producer;

    @Autowired
    private DiscussPostService discussPostService;

    /** 
    * @Description: 添加评论
    */
    //帖子回复后，重定向到原帖子，因此在请求中要有帖子ID。
    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        //用户没登录hostHolder会为空
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date()); 
        commentService.addComment(comment);

        //触发评论事件
        // 触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);  //存入帖子Id,在页面点击查看可以跳转到具体帖子
        //评论的目标：帖子
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            //查询帖子
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            //保存帖子的作者
            event.setEntityUserId(target.getUserId());
        }
        //评论的目标：评论
        else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            //根据id查询评论(被评论的id)
            Comment target = commentService.findCommentById(comment.getEntityId());
            //保存评论的作者(根据查询出的评论查找作者id)
            event.setEntityUserId(target.getUserId());
        }
        //发送消息
        Producer.fireEvent(event);
        return "redirect:/discuss/detail/" + discussPostId;
    }
}


