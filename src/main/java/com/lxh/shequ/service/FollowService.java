package com.lxh.shequ.service;
import	java.awt.Desktop.Action;

import com.lxh.shequ.entity.User;
import com.lxh.shequ.util.CommunityConstant;
import com.lxh.shequ.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: shequ
 * @description: 关注
 * @author: KaiDo
 * @return:
 * @create: 2020-04-12 16:15
 **/
@Service
public class FollowService implements CommunityConstant{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;
    /** 
    * @Description: 关注 ,存储粉丝和关注
    */
    public void follow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                //关注key
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
                //粉丝key
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                //开启事务
                redisOperations.multi();
                //关注逻辑：存储关注，粉丝
                redisOperations.opsForZSet().add(followeeKey, entityId , System.currentTimeMillis());
                redisOperations.opsForZSet().add(followerKey,userId,System.currentTimeMillis());
                return redisOperations.exec();
            }
        });
    }

    /**
    * @Description: 取消关注
    */
    public void unfollow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                //关注key
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
                //粉丝key
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                //开启事务
                redisOperations.multi();
                //取消关注逻辑：删除关注，粉丝
                redisOperations.opsForZSet().remove(followeeKey, entityId);
                redisOperations.opsForZSet().remove(followerKey,userId);
                return redisOperations.exec();
            }
        });
    }

    /** 
    * @Description: 查询用户关注的实体(用户/帖子)数量
    */
    public long findFolloweeCount(int userId,int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }
    
    /** 
    * @Description: 查询实体(用户/帖子)的粉丝的数量
    */
    public long findFollowerCount(int entityId,int entityType){
        String followerkey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerkey);
    }
    
    /** 
    * @Description: 查询当前用户是否已关注该实体
    */
    public boolean hasFollowed(int userId,int entityType,int entityId){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
    }
    
    /** 
    * @Description: 查询用户关注的人 ,分页
    */
    public List<Map<String, Object>> findFollowee(int userId,int offset,int limit){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
        //为什么要减一，因为offset是包含在其中的，按照时间从大到小排序
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);
        //如果没有关注的人，返回null
        if(targetIds==null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        for(int targetId :targetIds){
            User user = userService.findUserById(targetId);
            map.put("user", user);
            //之前存的score是时间毫秒
            Double followTime = redisTemplate.opsForZSet().score(followeeKey, targetId);
            //之前存的时间是毫秒值，在这里进行转换
            map.put("followTime",new Date(followTime.longValue()));
            list.add(map);
        }
        return list;
    }
    /** 
    * @Description: 查询用户的粉丝，分页
    */
    // 查询某用户的粉丝
    public List<Map<String, Object>> findFollowers(int userId, int offset, int limit) {
        String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
        //倒序，返回的虽然是Set,但是Redis使用的是Set接口，具体实现是有序的。
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);
        if (targetIds == null) {
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(targetId);
            map.put("user", user);
            Double score = redisTemplate.opsForZSet().score(followerKey, targetId);
            map.put("followTime", new Date(score.longValue()));
            list.add(map);
        }
        return list;
    }
}
