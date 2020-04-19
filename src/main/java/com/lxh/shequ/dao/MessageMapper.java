package com.lxh.shequ.dao;

import com.lxh.shequ.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    //查询当前用户的会话列表,针对每个会话只返回一条最新的私信(分页);相当于多个好友的消息列表
    List<Message> selectConversations(int userId,int offset,int limit);

    //查询当前用户的会话数量
    int selectConversationCount(int userId);

    //查询某个会话所包含的私信列表;相当于一个好友的聊天记录列表
    List<Message> selectLetters(String conversationId,int offset,int limit);

    //查询某个会话包含的私信数量
    int selectLetterCount(String conversationId);

    //查询未读私信的数量、查询未读的会话的数量
    int selectLetterUnreadCount(int userId,String conversationId);

    // 新增消息
    int insertMessage(Message message);

    // 修改消息的状态  未读:已读
    int updateStatus(List<Integer> ids, int status);

    //查询某个主题最新的通知（即最后一条评论/点赞/关注消息）
    Message selectLatestNotice(int userId,String topic);

    //查询某个主题包含的通知数量
    int selectNoticeCount(int userId,String topic);

    //查询未读的通知的数量,当没有传入topic时，可以查全部topic数量。就不用对三个主题进行加的操作
    int selectNoticeUnreadCount(int userId,String topic);

    //查询某个主题包含的通知列表：分页查询
    List<Message> selectNotices(int userId,String topic,int offset,int limit);
}
