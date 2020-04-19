package com.lxh.shequ.dao;

import com.lxh.shequ.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    //userId是个人用户使用的，可以查看个人帖子。后两个为分页功能
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在（动态SQL）<if>里使用,则必须加别名.
    // 根据ID查看总行数，首页index ID为0
    int selectDiscussPostRows(@Param("userId") int userId);

    //发布帖子，插入数据
    int insertDiscussPost(DiscussPost discusspost);

    //根据Id查询帖子。可以用关联查询查询user表和discuss表，但是会产生耦合，因为场景用的不多
    //或者用redis存，再取，不会影响性能
    DiscussPost selectDiscussPostById(int id);

    //新增评论时，更新评论数目，通过在实体类设置评论数目，之后查询评论数目可以不用在数据库遍历查询，而是直接获得。
    int updateCommentCount(int id, int commentCount);
}
