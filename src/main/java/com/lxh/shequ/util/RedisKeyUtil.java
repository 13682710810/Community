package com.lxh.shequ.util;

/**
 * @program: shequ
 * @description: Redis key生成
 * @author: KaiDo
 * @return:
 * @create: 2020-04-12 00:45
 **/
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    //实体赞
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    //用户赞,记录用户点了多少赞
    private static final String PREFIX_USER_LIKE = "like:user";
    //关注目标
    private static final String PREFIX_FOLLOWEE = "followee";
    //粉丝
    private static final String PREFIX_FOLLOWER = "follower";
    //验证码
    private static final String PREFIX_KAPTCHA = "kaptcha";
    //凭证
    private static final String PREFIX_TICKET = "ticket";
    //用户缓存
    private static final String PREFIX_USER = "user";

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)，用set集合存userId,除了可以知道赞的数目，还能根据Id知道是谁赞的。
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞，个人用户页面显示
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    //key的构成：某个用户关注的实体：userId:谁关注的， entityType:关注的实体
    //Value采用zset有序存储，存的是entityId(具体关注了什么), now（当前时间）
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝:
    //entityType跟entityId表示一个实体，存储的是粉丝(用户)
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    // 登录验证码，在页面传入owner(短时间的随机字符串)，标识验证码属于哪个用户
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录的凭证
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 用户缓存
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

}
