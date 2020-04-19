package com.lxh.shequ.controller;

import com.lxh.shequ.annotation.LoginRequired;
import com.lxh.shequ.entity.User;
import com.lxh.shequ.service.FollowService;
import com.lxh.shequ.service.LikeService;
import com.lxh.shequ.service.UserService;
import com.lxh.shequ.util.CommunityConstant;
import com.lxh.shequ.util.CommunityUtil;
import com.lxh.shequ.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @program: shequ
 * @description: 账号设置，上传用户头像,个人主页查用户关注
 * @author: KaiDo
 * @return:
 * @create: 2020-04-10 01:16
 **/
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    //上传位置
    @Value("${community.path.upload}")
    private String uploadPath;

    //域名
    @Value("${community.path.domain}")
    private String domain;

    //访问路径
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;
    /** 
    * @Description: 账号设置跳转
    */
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }
    
    /** 
    * @Description: 上传文件    Spring MVC的MultipartFile
    */
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }
        //文件原始文件名
        String fileName = headerImage.getOriginalFilename();
        //文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            return "/site/setting";
        }

        // 生成随机文件名
        fileName = CommunityUtil.getUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            //日志记录，异常进行统一处理
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8080/shequ/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateUserHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }

    /**
    * @Description: 获取头像
    */
    @LoginRequired
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (   //放在try里会自动关闭
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            //缓冲区
            byte[] buffer = new byte[1024];
            int b = 0;
            //不等于-1，说明读到了数据
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }
    
    /** 
    * @Description: 个人主页,当前用户/其他人
    */
    @RequestMapping(path = "/profile/{userId}",method = RequestMethod.GET)
    public String getPrePage(@PathVariable("userId") int userId,Model model){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        //用户信息
        model.addAttribute("user",user);
        //用户所得赞
        int userLikeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("userLikeCount",userLikeCount);
        //查询用户的粉丝数量
        long followerCount = followService.findFollowerCount(userId, 3);
        model.addAttribute("followerCount", followerCount);
        //查询用户的关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);
        //查询当前登录用户对该用户是否已关注
        boolean hasFollowed =false;
        //判断当前用户是否登录，没登录无法看到已关注
        User usera=hostHolder.getUser();
        if(usera!= null){
            hasFollowed = followService.hasFollowed(usera.getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);
        return "/site/profile";
    }

}
