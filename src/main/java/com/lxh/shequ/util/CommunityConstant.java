package com.lxh.shequ.util;

/** 
* @Description: 定义常用的常量 
*/
public interface CommunityConstant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证的超时时间(即没有勾上记住我),12小时
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态的登录凭证超时时间 100天
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * 实体类型: 帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型: 评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
    * @Description: 用户
    */
    int ENTITY_TYPE_USER = 3;

    /**
    * @Description:  主题-评论
    */
    String TOPIC_COMMENT = "comment";

    /**
    * @Description:  主题-点赞
    */
    String TOPIC_LIKE = "like";

    /**
    * @Description: 主题-关注
    */
    String TOPIC_FOLLOW = "follow";

    /** 
    * @Description: 系统用ID
    */
    int SYSTEM_ID = 1;
}
